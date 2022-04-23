package view;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class WordSearcher
  extends JFrame
{
  private static final long serialVersionUID = 4921753682582036207L;
  private JTextArea subText;
  private JButton ok = new JButton("OK");
  private JButton next = new JButton("  >>  ");
  
  public WordSearcher()
  {
    setTitle("Keres?");
    setSize(435, 280);
    setLocation(400, 300);
    setLayout(null);
    this.subText = new JTextArea();
    this.subText.setBounds(10, 20, 400, 150);
    this.subText.setFont(new Font("Arial", 0, 14));
    add(this.subText);
    this.next.setBounds(220, 190, 60, 30);
    add(this.next);
    this.ok.setBounds(140, 190, 60, 30);
    add(this.ok);
  }
  
  public JButton getButton()
  {
    return this.ok;
  }
  
  public JButton getNext()
  {
    return this.next;
  }
  
  public String getText()
  {
    return this.subText.getText();
  }
}
