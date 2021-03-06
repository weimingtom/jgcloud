/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RotationPanel.java
 *
 * Created on 20-Apr-2010, 22:44:21
 */
package worldeditor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Richard
 */
public class RotationPanel extends javax.swing.JPanel {

    private List<RotationChangeListener> listeners = new ArrayList<RotationChangeListener>();
    private NumberFormat formatter = new DecimalFormat("000.0");

    /** Creates new form RotationPanel */
    public RotationPanel() {
        initComponents();
    }

    public void addRotationChangeListener(RotationChangeListener listener) {
        this.listeners.add(listener);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xValueSlider = new javax.swing.JSlider();
        yValueSlider = new javax.swing.JSlider();
        zValueSlider = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        xValueTextField = new javax.swing.JTextField();
        yValueTextField = new javax.swing.JTextField();
        zValueTextField = new javax.swing.JTextField();

        xValueSlider.setMaximum(3600);
        xValueSlider.setValue(0);
        xValueSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                xValueSliderStateChanged(evt);
            }
        });

        yValueSlider.setMaximum(3600);
        yValueSlider.setValue(0);
        yValueSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                yValueSliderStateChanged(evt);
            }
        });

        zValueSlider.setMaximum(3600);
        zValueSlider.setValue(0);
        zValueSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                zValueSliderStateChanged(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("X");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Y");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Z");

        xValueTextField.setEditable(false);
        xValueTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        xValueTextField.setText("000.0");

        yValueTextField.setEditable(false);
        yValueTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        yValueTextField.setText("000.0");

        zValueTextField.setEditable(false);
        zValueTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        zValueTextField.setText("000.0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(zValueSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                    .addComponent(xValueSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                    .addComponent(yValueSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(xValueSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(xValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(yValueSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(yValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(zValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                            .addComponent(zValueSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                        .addGap(139, 139, 139))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tellListeners() {
        for (RotationChangeListener listener : listeners) {
            listener.rotationValueChanged(Float.parseFloat(xValueTextField.getText()), Float.parseFloat(yValueTextField.getText()), Float.parseFloat(zValueTextField.getText()));
        }
    }

    public void setValues(float xAngle, float yAngle, float zAngle) {
        xValueTextField.setText(formatter.format(xAngle));
        yValueTextField.setText(formatter.format(yAngle));
        zValueTextField.setText(formatter.format(zAngle));

        xValueSlider.setValue((int)(Float.parseFloat(xValueTextField.getText()) * 10f));
        yValueSlider.setValue((int)(Float.parseFloat(yValueTextField.getText()) * 10f));
        zValueSlider.setValue((int)(Float.parseFloat(zValueTextField.getText()) * 10f));
    }

    private void xValueSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_xValueSliderStateChanged
        xValueTextField.setText(formatter.format((float)xValueSlider.getValue()/10f));
        tellListeners();
    }//GEN-LAST:event_xValueSliderStateChanged

    private void yValueSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_yValueSliderStateChanged
        yValueTextField.setText(formatter.format((float)yValueSlider.getValue()/10f));
        tellListeners();
    }//GEN-LAST:event_yValueSliderStateChanged

    private void zValueSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_zValueSliderStateChanged
        zValueTextField.setText(formatter.format((float)zValueSlider.getValue()/10f));
        tellListeners();
    }//GEN-LAST:event_zValueSliderStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSlider xValueSlider;
    private javax.swing.JTextField xValueTextField;
    private javax.swing.JSlider yValueSlider;
    private javax.swing.JTextField yValueTextField;
    private javax.swing.JSlider zValueSlider;
    private javax.swing.JTextField zValueTextField;
    // End of variables declaration//GEN-END:variables
}
