package spatialviewer;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class SpatialDetailsTableModel extends AbstractTableModel {
    private static final String[] COLUMN_NAMES = { "Translation", "Rotation", "Scale" };
    List<SpatialInfo> spatialInfoRows;
    
    public SpatialDetailsTableModel(List<SpatialInfo> spatialInfoRows) {
        this.spatialInfoRows = spatialInfoRows;

        if (spatialInfoRows == null) {
            throw new NullPointerException("spatialInfoRows passed in to constructor is null.");
        }
    }

    public int getRowCount() {
        return spatialInfoRows.size();
    }

    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        SpatialInfo spatialInfo = spatialInfoRows.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return spatialInfo.getTranslation();
            case 1:
                return spatialInfo.getRotation();
            case 2:
                return spatialInfo.getScale();
            default:
                return "column " + columnIndex + " is invalid";
        }
    }

    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }
}
