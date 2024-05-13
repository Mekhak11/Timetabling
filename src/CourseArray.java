import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class CourseArray {
    private Course[] elements;
    private int period;

    public CourseArray(int numOfCourses, int numOfSlots) {
        this.period = numOfSlots;
        this.elements = new Course[numOfCourses];

        for(int i = 1; i < this.elements.length; ++i) {
            this.elements[i] = new Course();
        }

    }

    public int getSlots(){
        return period;
    }

    public void readClashes(String filename) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(filename));
            StringTokenizer line = new StringTokenizer(file.readLine());

            for(int count = line.countTokens(); count > 0; count = line.countTokens()) {
                if (count > 1) {
                    int[] index = new int[count];

                    int i;
                    for(i = 0; line.hasMoreTokens(); ++i) {
                        index[i] = Integer.parseInt(line.nextToken());
                    }

                    for(i = 0; i < index.length; ++i) {
                        for(int j = 0; j < index.length; ++j) {
                            if (j != i) {
                                int k;
                                for(k = 0; k < this.elements[index[i]].clashesWith.size() && this.elements[index[i]].clashesWith.elementAt(k) != this.elements[index[j]]; ++k) {
                                }

                                if (k == this.elements[index[i]].clashesWith.size()) {
                                    this.elements[index[i]].addClash(this.elements[index[j]]);
                                }
                            }
                        }
                    }
                }

                line = new StringTokenizer(file.readLine());
            }

            file.close();
        } catch (Exception var9) {
        }

    }

    public int length() {
        return this.elements.length;
    }

    public int status(int index) {
        return this.elements[index].clashSize();
    }

    public int slot(int index) {
        return this.elements[index].mySlot;
    }

    public void setSlot(int index, int newSlot) {
        this.elements[index].mySlot = newSlot;
    }

    public int maxClashSize(int index) {
        return this.elements[index] != null && !this.elements[index].clashesWith.isEmpty() ? this.elements[index].clashesWith.size() : 0;
    }

    public int clashesLeft() {
        int result = 0;

        for(int i = 1; i < this.elements.length; ++i) {
            result += this.elements[i].clashSize();
        }

        return result;
    }

    public void iterate(int shifts) {
        for(int index = 1; index < this.elements.length; ++index) {
            this.elements[index].setForce();

            for(int move = 1; move <= shifts && this.elements[index].force != 0; ++move) {
                this.elements[index].setForce();
                this.elements[index].shift(this.period);
            }
        }

    }

    public void printResult() {
        for(int i = 1; i < this.elements.length; ++i) {
            System.out.println("" + i + "\t" + this.elements[i].mySlot);
        }
    }

    public int[] slotStatus(int slot) {
        int clashes = 0;
        int course = 0;
        int[] result = new int[2];

        for (int i = 1; i<elements.length; i++) {
            if(elements[i].mySlot == slot) {
                course++;
                clashes+=elements[i].clashSize();
            }
        }

        result[0] = course;
        result[1] = clashes;

        return  result;
    }

    public int[][] getClashFreeTimeSlots() {
        ArrayList<Integer> slots = new ArrayList<>();
        for(int i = 0; i < period; i++) {
            int[] result = slotStatus(i);
            if(result[1] == 0){
                slots.add(i);
            }
        }

        int[][] clashFreeTimeSlots = new int[slots.size()][elements.length];
        int index = 0;

        for(int t:slots) {
            for(int i = 1; i < elements.length; i++) {
                if(elements[i].mySlot == t){
                    clashFreeTimeSlots[index][i] = 1;
                    index++;
                }
            }
        }
        return  clashFreeTimeSlots;
    }

    public int[][] getAllTimeSlotStatus() {
        int[][] timeSlots = new int[period][2];
        for(int i = 0; i < period; i++) {
            timeSlots[i] = slotStatus(i);
        }
        return timeSlots;
    }

    public int[] getTimeSlot(int index) {
        int[] courses = new int[elements.length];

        for(int i = 1; i < elements.length; i++){
            if (elements[i].mySlot == index) {
                courses[i] = 1;
            } else {
                courses[i] = -1;
            }
        }
        return courses;
    }

}
