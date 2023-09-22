package OMS.UI.GUIFramework;

import OMS.domain.SalesReports.GenerateSalesReport;
import OMS.domain.SalesReports.IGetSalesReport;
import OMS.domain.SalesReports.SalesReportType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

import static OMS.domain.SalesReports.SalesReportType.CSV;
import static OMS.domain.SalesReports.SalesReportType.TXT;


public class SalesUIController {

    @FXML
    public DatePicker datePickerFrom, datePickerTo;
    @FXML
    public Button buttonTxt, buttonCsv;
    @FXML
    public TextArea completedSalesReport;
    @FXML
    public TextField orderId;

    SalesReportType type;
    IGetSalesReport iGetSalesReport = new GenerateSalesReport();


    public void initialize() {
        buttonPressed();
    }


    public LocalDate getDatePickerFrom() {
        return datePickerFrom.getValue();
    }

    public LocalDate getDatePickerTo() {
        return datePickerTo.getValue();
    }

    //method for buttons that check if there is an orderid input or not
    public void buttonPressed() {

        buttonTxt.setOnMouseClicked(event -> {
            type = TXT;
            if (!checkOrderID(orderId)) {
                iGetSalesReport.generateSalesReport(getDatePickerFrom(), getDatePickerTo(), type);
            } else {
                iGetSalesReport.generateSalesReport(getDatePickerFrom(), getDatePickerTo(), type, getOrderId());
            }
            completedSalesReport.setText("Completed txt file");
        });

        buttonCsv.setOnMouseClicked(event -> {
            type = CSV;
            if (!checkOrderID(orderId)) {
                iGetSalesReport.generateSalesReport(getDatePickerFrom(), getDatePickerTo(), type);
            } else {
                iGetSalesReport.generateSalesReport(getDatePickerFrom(), getDatePickerTo(), type, getOrderId());
            }
            completedSalesReport.setText("Completed csv file");
        });

    }

    //method to check if there is an input for orderid textfield
    public boolean checkOrderID(TextField textField) {
        return !textField.getText().isEmpty();
    }

    //method to get the text in order id textfield
    public int getOrderId() {
        int getOrderId = Integer.parseInt(orderId.getText());
        return getOrderId;
    }

}