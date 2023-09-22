package OMS.domain.SalesReports;

import java.time.LocalDate;

public interface IGetSalesReport {

    //Method to get sales reports
    SalesReport generateSalesReport(LocalDate startDate, LocalDate endDate, SalesReportType type, int... id);


}

