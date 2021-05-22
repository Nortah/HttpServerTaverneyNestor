import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class MyCustomFormatter extends Formatter {
    @Override
    public String format(LogRecord record){
        StringBuffer sb= new StringBuffer();
        Date date = new Date(record.getMillis());
        sb.append(date);
        sb.append(";");

        sb.append(record.getLevel().getName());
        sb.append(";");

        sb.append(record.getSourceClassName());
        sb.append(";");

        sb.append(record.getSourceMethodName());
        sb.append(";");

        sb.append(formatMessage(record));
        sb.append("\r\n");
        return sb.toString();

    }
}
