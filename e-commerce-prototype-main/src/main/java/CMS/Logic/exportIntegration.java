package CMS.Logic;

import java.sql.ResultSet;

public interface exportIntegration {

    public String getFilePath(String identifier);

    public ResultSet getExportData();
}
