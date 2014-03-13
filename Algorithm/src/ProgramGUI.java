import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.Border;
import java.awt.*;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class ProgramGUI extends JFrame {

    public ProgramGUI() {


        ImageIcon node_one = new ImageIcon(this.getClass().getResource("/images/computer.png"),"this is a caption");
        JLabel label_one = new JLabel("Node 1", node_one, JLabel.LEFT);


        ImageIcon node_two = new ImageIcon(this.getClass().getResource("/images/computer.png"),"this is a caption");
        JLabel label_two = new JLabel("Node 2", node_two, JLabel.CENTER);


        ImageIcon node_three = new ImageIcon(this.getClass().getResource("/images/computer.png"),"this is a caption");
        JLabel label_three = new JLabel("Node 3", node_three, JLabel.RIGHT);


        setTitle("EE4210 Project");
        add(label_one, BorderLayout.WEST);
        add(label_two, BorderLayout.SOUTH);
        add(label_three, BorderLayout.EAST);

        JTextArea textArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);


        add(textArea);

        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ProgramGUI ex = new ProgramGUI();
                ex.setVisible(true);
            }
        });

    }

}