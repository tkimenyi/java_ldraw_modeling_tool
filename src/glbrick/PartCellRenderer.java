package glbrick;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.Hashtable;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class PartCellRenderer extends DefaultListCellRenderer{
        @SuppressWarnings("rawtypes")
        private Hashtable iconTable = new Hashtable();

        @SuppressWarnings("unchecked")
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus) {  
                JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if(value instanceof PartLabel){
                        PartLabel res = (PartLabel)value;
                        ImageIcon icon = (ImageIcon)iconTable.get(value);
                        if(icon == null){
                                final String fileLoc = res.getIconFile();
                                icon = new ImageIcon(res.getIconFile()){
                                        @Override
                                        public void paintIcon(Component c, Graphics g, int x, int y){
                                                File file = new File(fileLoc);
                                                if(file.exists()){
                                                        g.drawImage(getImage(), 0, 0, 80, 80, c);
                                                }else{
                                                        Graphics2D g2 = (Graphics2D)g;
                                                        Rectangle rect = new Rectangle(0, 0, 80, 80);
                                                        g2.draw(rect);
                                                        float cx, cy;
                                                        float deltaX, deltaY;
                                                        deltaX = deltaY = 20;

                                                        cx = (float)rect.getCenterX();
                                                        cy = (float)rect.getCenterY();

                                                        float leftX = cx - deltaX;
                                                        float rightX = cx + deltaX;
                                                        float topY = cy - deltaY;
                                                        float bottomY = cy + deltaY;

                                                        g2.setColor(Color.RED);
                                                        g2.setStroke(new BasicStroke(5));
                                                        Line2D.Float line1 = new Line2D.Float(leftX, topY, rightX, bottomY);
                                                        Line2D.Float line2 = new Line2D.Float(leftX, bottomY, rightX, topY);
                                                        g2.draw(line2);
                                                        g2.draw(line1);
                                                }
                                        }
                                        @Override
                                        public int getIconHeight(){
                                                return 80;
                                        }
                                        @Override
                                        public int getIconWidth(){
                                                return 80;
                                        }

                                };
                                iconTable.put(value, icon);
                        }
                        label.setIcon(icon);
                        label.setText(res.getPartFile());
                        label.setHorizontalAlignment(SwingConstants.LEADING);

                }else{
                        label.setIcon(null);
                }
                return label;
        }

}