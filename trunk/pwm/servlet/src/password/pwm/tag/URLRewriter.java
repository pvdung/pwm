/*
 * Password Management Servlets (PWM)
 * http://code.google.com/p/pwm/
 *
 * Copyright (c) 2006-2009 Novell, Inc.
 * Copyright (c) 2009-2012 The PWM Project
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package password.pwm.tag;

import password.pwm.ContextManager;
import password.pwm.PwmApplication;
import password.pwm.PwmSession;
import password.pwm.SessionFilter;
import password.pwm.servlet.ResourceFileServlet;

import javax.servlet.jsp.JspTagException;

public class URLRewriter extends PwmAbstractTag {

    private String url;
    private static final String resource_url = "/resources";
// --------------------- GETTER / SETTER METHODS ---------------------

    public void setUrl(final String url)
    {
        this.url = url;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Tag ---------------------

    public int doEndTag()
            throws javax.servlet.jsp.JspTagException
    {
        try {
            String newURL = SessionFilter.rewriteURL(url, pageContext.getRequest(), pageContext.getResponse());
            if (newURL.contains(resource_url)) {
                final PwmApplication pwmApplication = ContextManager.getPwmApplication(pageContext.getSession());
                final PwmSession pwmSession = PwmSession.getPwmSession(pageContext.getSession());
                final String nonce = ResourceFileServlet.makeResourcePathNonce(pwmApplication, pwmSession);
                newURL = newURL.replace(resource_url, resource_url + nonce);
            }
            
            pageContext.getOut().write(newURL);
        } catch (Exception e) {
            throw new JspTagException(e.getMessage());
        }
        return EVAL_PAGE;
    }
}
