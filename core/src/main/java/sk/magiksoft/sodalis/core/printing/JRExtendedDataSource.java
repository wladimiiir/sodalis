package sk.magiksoft.sodalis.core.printing;

import net.sf.jasperreports.engine.JRRewindableDataSource;

import java.util.List;

/**
 * @author wladimiiir
 */
public interface JRExtendedDataSource extends JRRewindableDataSource {
    void setData(List data);
}
