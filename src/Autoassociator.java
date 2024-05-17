import java.util.Random;

public class Autoassociator {
    private int weights[][];
    private int trainingCapacity;

    private int counter;
    private Random rd;
    private  CourseArray courseArray;

    public Autoassociator(CourseArray courses) {
        counter = 0;

        courseArray = courses;

        rd = new Random();

        weights = new int[courses.length()][courses.length()];
    }

    public int getTrainingCapacity() {
        return weights.length;
    }

    public void training(int pattern[]) {
        for (int i = 1; i < weights.length; i++) {
            for (int j = 1; j < weights[i].length; j++) {
                if (i != j) {
                    weights[i][j] += pattern[i] * pattern[j];
                } else {
                    weights[i][j] = 0;
                }
            }
        }
        counter++;

    }

    public int unitUpdate(int neurons[]) {
        int index = rd.nextInt(neurons.length-1) + 1;
        unitUpdate(neurons,index);
        return index;
    }

    public void unitUpdate(int neurons[], int index) {
        int a = 0;
        for(int i = 1;i<neurons.length;i++){
            a+=weights[index][i] * neurons[i];
        }
        neurons[index] = a>0?1:-1;
    }

    public void chainUpdate(int neurons[], int steps) {
        for(int i = 0;i<steps;i++){
            unitUpdate(neurons);
        }
    }

    public int getTrainCounter(){
        return counter;
    }

    public void fullUpdate(int neurons[]) {
    }

    public boolean needTrain(){
        return counter<= weights.length*0.139;
    }
}

