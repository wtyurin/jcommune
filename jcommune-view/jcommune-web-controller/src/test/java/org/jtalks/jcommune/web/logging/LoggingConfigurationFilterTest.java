/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.jcommune.web.logging;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.jtalks.common.security.SecurityService;
import org.jtalks.jcommune.web.filters.LoggingConfigurationFilter;
import org.mockito.Mock;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 
 * @author Anuar_Nurmakanov
 *
 */
public class LoggingConfigurationFilterTest {
    @Mock
    private SecurityService securityService;
    @Mock
    private LoggingMDCService loggingMDCService;
    //
    private LoggingConfigurationFilter loggingConfigurationFilter;
    
    @BeforeMethod
    public void init() {
        initMocks(this);
        this.loggingConfigurationFilter = new LoggingConfigurationFilter(
                securityService, loggingMDCService);
    }
    
    @Test
    public void userShouldBeRegisteredAndUnregisteredWhenChainEnded() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        String userName = "Shogun";
        when(securityService.getCurrentUserUsername()).thenReturn(userName);
        
        loggingConfigurationFilter.doFilter(request, response, filterChain);
        
        verify(loggingMDCService).registerUser(userName);
        verify(loggingMDCService).unregisterUser();
    }
    
    @Test
    public void emptyUserNameShouldNotBeRegisteredAndUnregistered() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        String userName = StringUtils.EMPTY;
        when(securityService.getCurrentUserUsername()).thenReturn(userName);
        
        loggingConfigurationFilter.doFilter(request, response, filterChain);
        
        verify(loggingMDCService, never()).registerUser(userName);
        verify(loggingMDCService, never()).unregisterUser();  
    }
}