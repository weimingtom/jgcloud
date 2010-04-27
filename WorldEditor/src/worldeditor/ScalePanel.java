/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TranslationPanel.java
 *
 * Created on 21-Apr-2010, 08:23:18
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
public class ScalePanel extends javax.swing.JPanel {
    private List<ScaleChangeListener> listeners = new ArrayList<ScaleChangeListener>();
    private NumberFormat formatter = new DecimalFormat("#########0.00");

    /** Creates new form TranslationPanel */
    public ScalePanel() {
        initComponents();
    }

    public void addScaleChangeListener(ScaleChangeListener listener) {
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

        jLabel1 = new javax.swing.JLabel();
        xValueTextField = new javax.swing.JTextField();
        xLowerButton = new javax.swing.JButton();
        xRaiseButton = new javax.swing.JButton();
        yRaiseButton = new javax.swing.JButton();
        yLowerButton = new javax.swing.JButton();
        yValueTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        zRaiseButton = new javax.swing.JButton();
        zLowerButton = new javax.swing.JButton();
        zValueTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        valueIncrementsComboBox = new javax.swing.JComboBox();

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("X");

        xValueTextField.setEditable(false);
        xValueTextField.setText("1.00");

        xLowerButton.setText("<");
        xLowerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xLowerButtonActionPerformed(evt);
            }
        });

        xRaiseButton.setText(">");
        xRaiseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xRaiseButtonActionPerformed(evt);
            }
        });

        yRaiseButton.setText(">");
        yRaiseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yRaiseButtonActionPerformed(evt);
            }
        });

        yLowerButton.setText("<");
        yLowerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yLowerButtonActionPerformed(evt);
            }
        });

        yValueTextField.setEditable(false);
        yValueTextField.setText("1.00");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Y");

        zRaiseButton.setText(">");
        zRaiseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zRaiseButtonActionPerformed(evt);
            }
        });

        zLowerButton.setText("<");
        zLowerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zLowerButtonActionPerformed(evt);
            }
        });

        zValueTextField.setEditable(false);
        zValueTextField.setText("1.00");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Z");

        valueIncrementsComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00.01", "00.10", "00.50", "01.00", "05.00", "10.00", "50.00" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xLowerButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xRaiseButton)
                        .addGap(18, 18, 18)
                        .addComponent(valueIncrementsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yLowerButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yRaiseButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(zValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(zLowerButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(zRaiseButton)))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xLowerButton)
                    .addComponent(xRaiseButton)
                    .addComponent(valueIncrementsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yLowerButton)
                    .addComponent(yRaiseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zLowerButton)
                    .addComponent(zRaiseButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void xLowerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xLowerButtonActionPerformed
        float currentValue = Float.parseFloat(xValueTextField.getText());
        float currentIncrement = Float.parseFloat(valueIncrementsComboBox.getSelectedItem().toString());
        currentValue -= currentIncrement;
        xValueTextField.setText(formatter.format(currentValue));
        tellListeners();
    }//GEN-LAST:event_xLowerButtonActionPerformed

    private void xRaiseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xRaiseButtonActionPerformed
        float currentValue = Float.parseFloat(xValueTextField.getText());
        float currentIncrement = Float.parseFloat(valueIncrementsComboBox.getSelectedItem().toString());
        currentValue += currentIncrement;
        xValueTextField.setText(formatter.format(currentValue));
        tellListeners();
    }//GEN-LAST:event_xRaiseButtonActionPerformed

    private void yLowerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yLowerButtonActionPerformed
        float currentValue = Float.parseFloat(yValueTextField.getText());
        float currentIncrement = Float.parseFloat(valueIncrementsComboBox.getSelectedItem().toString());
        currentValue -= currentIncrement;
        yValueTextField.setText(formatter.format(currentValue));
        tellListeners();
    }//GEN-LAST:event_yLowerButtonActionPerformed

    private void yRaiseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yRaiseButtonActionPerformed
        float currentValue = Float.parseFloat(yValueTextField.getText());
        float currentIncrement = Float.parseFloat(valueIncrementsComboBox.getSelectedItem().toString());
        currentValue += currentIncrement;
        yValueTextField.setText(formatter.format(currentValue));
        tellListeners();
    }//GEN-LAST:event_yRaiseButtonActionPerformed

    private void zLowerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zLowerButtonActionPerformed
        float currentValue = Float.parseFloat(zValueTextField.getText());
        float currentIncrement = Float.parseFloat(valueIncrementsComboBox.getSelectedItem().toString());
        currentValue -= currentIncrement;
        zValueTextField.setText(formatter.format(currentValue));
        tellListeners();
    }//GEN-LAST:event_zLowerButtonActionPerformed

    private void zRaiseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zRaiseButtonActionPerformed
        float currentValue = Float.parseFloat(zValueTextField.getText());
        float currentIncrement = Float.parseFloat(valueIncrementsComboBox.getSelectedItem().toString());
        currentValue += currentIncrement;
        zValueTextField.setText(formatter.format(currentValue));
        tellListeners();
    }//GEN-LAST:event_zRaiseButtonActionPerformed

    private void tellListeners() {
        float xValue = Float.parseFloat(xValueTextField.getText());
        float yValue = Float.parseFloat(yValueTextField.getText());
        float zValue = Float.parseFloat(zValueTextField.getText());

        for (ScaleChangeListener listener : listeners) {
            listener.scaleValueChanged(xValue, yValue, zValue);
        }
    }

    public void setValues(float xVal, float yVal, float zVal) {
        xValueTextField.setText(formatter.format(xVal));
        yValueTextField.setText(formatter.format(yVal));
        zValueTextField.setText(formatter.format(zVal));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JComboBox valueIncrementsComboBox;
    private javax.swing.JButton xLowerButton;
    private javax.swing.JButton xRaiseButton;
    private javax.swing.JTextField xValueTextField;
    private javax.swing.JButton yLowerButton;
    private javax.swing.JButton yRaiseButton;
    private javax.swing.JTextField yValueTextField;
    private javax.swing.JButton zLowerButton;
    private javax.swing.JButton zRaiseButton;
    private javax.swing.JTextField zValueTextField;
    // End of variables declaration//GEN-END:variables

}
