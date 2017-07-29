package pruebas.reportes.jasperreports.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author Alex
 */
public class ServletReporte extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // response.setHeader("Content-Disposition", "attachment; filename=\"reporte.pdf\";");  // con esta linea se descarga el pdf y no permite visualizar en el navegador
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        response.setContentType("application/pdf");
        

        ServletOutputStream out = response.getOutputStream();

        List listaPariticipantes = new ArrayList();

        for (int i = 1; i <= 10; i++)
        {
            Participante p = new Participante(i, "Particpante " + i, "Usuario " + i, "Pass " + i, "Comentarios para " + i);
            p.setPuntos(i);
            listaPariticipantes.add(p);
        }

        try
        {
            System.out.println("getRealPath: " + getServletContext().getRealPath("/WEB-INF/reporte2.jasper"));
            JasperReport reporte = (JasperReport) JRLoader.loadObject(getServletContext().getRealPath("/WEB-INF/reporte2.jasper"));

            Map parametros = new HashMap();
            parametros.put("autor", "Juan");
            parametros.put("titulo", "Reporte");

            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros, new JRBeanCollectionDataSource(listaPariticipantes));

            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
            exporter.exportReport();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo()
    {
        return "Short description";
    }
}
