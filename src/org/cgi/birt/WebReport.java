package org.cgi.birt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;

@SuppressWarnings("deprecation")
@WebServlet(value = "/WebReport")
public class WebReport extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor of the object.
	 */
	private IReportEngine birtReportEngine = null;
	protected static Logger logger = Logger.getLogger("org.eclipse.birt");

	public WebReport() {
		super();
	}

	/**
	 * Destruction of the servlet.
	 * 
	 */
	public void destroy() {
		super.destroy();
		BirtEngine.destroyBirtEngine();
	}

	/**
	 * The doGet method of the servlet.
	 *
	 * 
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(req.getParameter("docType") == null) {
			//default is PDF
			generatePDF(req, resp);
		} else if("HTML".equalsIgnoreCase(req.getParameter("docType"))) {
			generateHTML(req, resp);
		} else {
			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();
			out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
			out.println("<HTML>");
			out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
			out.println("  <BODY>");
			out.println(" Document Type is not supported");
			out.println("  </BODY>");
			out.println("</HTML>");
			out.flush();
			out.close();
		}
		
	}

	/**
	 * The doPost method of the servlet.
	 *
	 * 
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.println(" Post does nothing");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet.
	 *
	 * 
	 * @throws ServletException
	 *             if an error occure
	 */
	public void init() throws ServletException {
		BirtEngine.initBirtConfig();

	}

	@SuppressWarnings({ "unused", "deprecation" })
	private void generatePDF(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		String reportName = BirtEngine.configProps.getProperty("reportname");
		resp.setContentType("application/pdf");
		resp.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".pdf");
		// String reportName = req.getParameter("ReportName");

		ServletContext sc = req.getSession().getServletContext();
		this.birtReportEngine = BirtEngine.getBirtEngine(sc);

		// setup image directory
		HTMLRenderContext renderContext = new HTMLRenderContext();
		renderContext.setBaseImageURL(req.getContextPath() + "/images");
		renderContext.setImageDirectory(sc.getRealPath("/images"));

		logger.log(Level.FINE, "image directory " + sc.getRealPath("/images"));
		System.out.println("stdout image directory " + sc.getRealPath("/images"));

		HashMap<String, HTMLRenderContext> contextMap = new HashMap<String, HTMLRenderContext>();
		contextMap.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext);

		IReportRunnable design;
		try {
			// Open report design
			design = birtReportEngine.openReportDesign(sc.getRealPath("/reports") + "/" + reportName);
			// create task to run and render report
			IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);
			task.setAppContext(contextMap);

			// set output options
			HTMLRenderOption options = new HTMLRenderOption();
			options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
			options.setOutputStream(resp.getOutputStream());
			task.setRenderOption(options);

			// run report
			task.run();
			task.close();
		} catch (Exception e) {

			e.printStackTrace();
			throw new ServletException(e);
		}

	}

	@SuppressWarnings({ "unused", "deprecation" })
	private void generateHTML(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		String reportName =BirtEngine.configProps.getProperty("reportname");
		resp.setContentType("text/html");
		// String reportName = req.getParameter("ReportName");

		ServletContext sc = req.getSession().getServletContext();
		this.birtReportEngine = BirtEngine.getBirtEngine(sc);

		// setup image directory
		HTMLRenderContext renderContext = new HTMLRenderContext();
		renderContext.setBaseImageURL(req.getContextPath() + "/images");
		renderContext.setImageDirectory(sc.getRealPath("/images"));

		logger.log(Level.FINE, "image directory " + sc.getRealPath("/images"));
		System.out.println("stdout image directory " + sc.getRealPath("/images"));

		HashMap<String, HTMLRenderContext> contextMap = new HashMap<String, HTMLRenderContext>();
		contextMap.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext);

		IReportRunnable design;
		try {
			// Open report design
			design = birtReportEngine.openReportDesign(sc.getRealPath("/reports") + "/" + reportName);
			// create task to run and render report
			IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);
			task.setAppContext(contextMap);

			// set output options
			HTMLRenderOption options = new HTMLRenderOption();
			options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
			options.setOutputStream(resp.getOutputStream());
			task.setRenderOption(options);

			// run report
			task.run();
			task.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}

	}

}
