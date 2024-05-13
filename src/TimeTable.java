import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import javax.swing.*;

public class TimeTable extends JFrame implements ActionListener {

    private JPanel screen = new JPanel(), tools = new JPanel();
    private JButton tool[];
    private JTextField field[];
    private CourseArray courses;
    private Color CRScolor[] = {Color.RED, Color.GREEN, Color.BLACK};

    private Autoassociator autoassociator;
    int min = Integer.MAX_VALUE, step;
    int iterations = 0;
    Random random;

    public TimeTable() {
        super("Dynamic Time Table");
        setSize(500, 800);
        setLayout(new FlowLayout());
        screen.setPreferredSize(new Dimension(400, 800));
        add(screen);
        setTools();
        add(tools);
        random = new Random();
        setVisible(true);
    }

    public void setTools() {
        String capField[] = {"Slots:", "Courses:", "Clash File:", "Iters:", "Shift:"};
        field = new JTextField[capField.length];

        String capButton[] = {"Load", "Start", "Cont", "Step", "Train", "Update" , "Print", "Exit"};
        tool = new JButton[capButton.length];

        tools.setLayout(new GridLayout(2 * capField.length + capButton.length, 1));

        for (int i = 0; i < field.length; i++) {
            tools.add(new JLabel(capField[i]));
            field[i] = new JTextField(5);
            tools.add(field[i]);
        }

        for (int i = 0; i < tool.length; i++) {
            tool[i] = new JButton(capButton[i]);
            tool[i].addActionListener(this);
            tools.add(tool[i]);
        }

        field[0].setText("22");
        field[1].setText("190");
        field[2].setText("ear-f-83.stu");
        field[3].setText("1");
    }

    public void draw() {
        Graphics g = screen.getGraphics();
        int width = Integer.parseInt(field[0].getText()) * 10;
        for (int courseIndex = 1; courseIndex < courses.length(); courseIndex++) {
            g.setColor(CRScolor[courses.status(courseIndex) > 0 ? 0 : 1]);
            g.drawLine(0, courseIndex, width, courseIndex);
            g.setColor(CRScolor[CRScolor.length - 1]);
            g.drawLine(10 * courses.slot(courseIndex), courseIndex, 10 * courses.slot(courseIndex) + 10, courseIndex);
        }
    }

    private int getButtonIndex(JButton source) {
        int result = 0;
        while (source != tool[result]) result++;
        return result;
    }

    public void actionPerformed(ActionEvent click) {
        int clashes;

        switch (getButtonIndex((JButton) click.getSource())) {
            case 0:
                int slots = Integer.parseInt(field[0].getText());
                courses = new CourseArray(Integer.parseInt(field[1].getText()) + 1, slots);
                courses.readClashes(field[2].getText());
                autoassociator = new Autoassociator(courses);
                draw();
                break;
            case 1:
                iterations = 0;
                min = Integer.MAX_VALUE;
                step = 0;
                for (int i = 1; i < courses.length(); i++) courses.setSlot(i, 0);

                for (int iteration = 1; iteration <= Integer.parseInt(field[3].getText()); iteration++) {
                    courses.iterate(Integer.parseInt(field[4].getText()));
                    draw();
                    clashes = courses.clashesLeft();
                    if (clashes < min) {
                        min = clashes;
                        step = iteration;
                    }
                    iterations++;
                }

                String str = "Shift = " + field[4].getText() + "\tMin clashes = " + min + "\tat step " + step;
                log(str);
                System.out.println(str);
                setVisible(true);

                break;
            case 2:
                int temp = Integer.parseInt(field[3].getText());
                for (int iteration = 1; iteration <= temp; iteration++) {
                    courses.iterate(Integer.parseInt(field[4].getText()));
                    draw();
                    iterations++;
                    clashes = courses.clashesLeft();
                    if (clashes < min) {
                        min = clashes;
                        step = iterations;
                    }

                }
                String s = "Shift = " + field[4].getText() + "\tMin clashes = " + min + "\tat step " + step;
                System.out.println(s);
                log(s);
                setVisible(true);
            case 3:
                courses.iterate(Integer.parseInt(field[4].getText()));
                draw();
                break;
            case 4:
                train();
                log("Trained Amount:" + autoassociator.getTrainCounter());
                System.out.println(autoassociator.getTrainCounter());
                break;
            case 5:
                for(int i = 0;i<courses.getSlots();i++){
                    if(courses.slotStatus(i)[1]==0){
                        continue;
                    }
                    update(i);
                }
                clashes = courses.clashesLeft();
                System.out.println("Clashes " + clashes);
                log("Clashes " + clashes);
                draw();
                break;
            case 6:
                System.out.println("Exam\tSlot\tClashes");
                for (int i = 1; i < courses.length(); i++)
                    System.out.println(i + "\t" + courses.slot(i) + "\t" + courses.status(i));
                break;
            case 7:
                System.exit(0);
        }
    }

    public  void update(int slot) {
        int[] timeSlots = courses.getTimeSlot(slot);
        int index = autoassociator.unitUpdate(timeSlots);
        int[] tempSlots = courses.getTimeSlot(slot);
        if(timeSlots[index] != tempSlots[index]) {
            if(timeSlots[index] == 1) {
                courses.setSlot(index,slot);
            }else{
                courses.setSlot(index,random.nextInt(courses.getSlots()+1)-1);
            }
        }
    }

    public void train() {
        int[][] slots = courses.getAllTimeSlotStatus();
        for(int i=0; i<slots.length; i++) {
            System.out.println(slots[i][0] + " " + slots[i][1]);
        }
        int min = (int)(((double) (courses.length() - 1)) / (courses.getSlots() + 1) * 0.5);
        System.out.println(min);
        for (int i = 0; i < slots.length; i++) {
            if (slots[i][1] == 0) {
                if (slots[i][0] > min) {
                    if (autoassociator.needTrain()) {
                        int[] temp = courses.getTimeSlot(i);
                        autoassociator.training(temp);
                    }
                }
            }
        }
    }

    public void log(String str){
        String filename = "logs/log.txt";
        String content = str;
        try {
            FileWriter fileWriter = new FileWriter(filename,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new TimeTable();
    }

}
