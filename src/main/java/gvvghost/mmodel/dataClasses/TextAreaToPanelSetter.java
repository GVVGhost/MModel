package gvvghost.mmodel.dataClasses;

import gvvghost.mmodel.calc.C;
import javafx.scene.control.TextArea;

public class TextAreaToPanelSetter implements Callback{
    TextArea myTextArea;
    public TextAreaToPanelSetter(TextArea textArea){
        myTextArea = textArea;
    }

    private void setToLabel(Recordable record){
        StringBuilder line = new StringBuilder();
        SimulationPoint point = (SimulationPoint) record;
        line.append("SN:").append(point.getSequenceNumber()+1);
        for (Value value : point.getValues()){
            line.append(" ").append(value.getParam().getName()).append(":").append(C.roundAvoid(value.getValue(), 3));
        }
        myTextArea.setText(line.toString());
    }

    @Override
    public void callingBack(Recordable record) {
        setToLabel(record);
    }
}
