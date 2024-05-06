import java.util.Vector;

public class Course {
    public Vector<Course> clashesWith = new Vector();
    public int mySlot;
    public int force;

    public Course() {
        this.mySlot = 0;
    }

    public Course(int newSlot) {
        this.mySlot = newSlot;
    }

    public void addClash(Course thatClash) {
        this.clashesWith.add(thatClash);
    }

    public int clashSize() {
        int result = 0;

        for(int i = 0; i < this.clashesWith.size(); ++i) {
            if (this.mySlot == ((Course)this.clashesWith.elementAt(i)).mySlot) {
                ++result;
            }
        }

        return result;
    }

    public int unitClashForce() {
        for(int i = 0; i < this.clashesWith.size(); ++i) {
            if (this.mySlot == ((Course)this.clashesWith.elementAt(i)).mySlot) {
                return 1;
            }
        }

        return 0;
    }

    public void setForce() {
        this.force = this.unitClashForce();
    }

    public void shift(int limit) {
        this.mySlot += this.force;
        if (this.mySlot < 0) {
            this.mySlot = limit - 1;
        } else if (this.mySlot >= limit) {
            this.mySlot = 0;
        }

    }
}

