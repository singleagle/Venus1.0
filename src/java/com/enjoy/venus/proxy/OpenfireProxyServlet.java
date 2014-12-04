package com.enjoy.venus.proxy;

import javax.servlet.http.HttpServlet;

import org.jivesoftware.openfire.XMPPServer;

public class OpenfireProxyServlet extends HttpServlet{
	XMPPServer mOpenfireServer;
	
	public OpenfireProxyServlet(){
		//ServerStarter.main();
		mOpenfireServer = new XMPPServer();
	}
	
}
