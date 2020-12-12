import java.util.ArrayList;
import java.util.Scanner;

class flight {
    String fName;
    int fWeight;
    int timeHalt;

    public flight(String fName, int fWeight, int timeHalt) {
        this.fName = fName;
        this.fWeight = fWeight;
        this.timeHalt = timeHalt;
    }

    int computeTime(int weight) {
        int percentage = (weight / fWeight) * 100;
        int ans;
        if (percentage > 75) {
            ans = timeHalt;
        } else if (percentage > 50 && percentage < 75) {
            ans = timeHalt - (timeHalt * (10 / 100));
        } else ans = timeHalt - (timeHalt * (20 / 100));
        return ans + 10;
    }
}

class runway {
    String rName;
    int rTime;
    boolean status;

    public runway(String rName, int rTime, boolean status) {
        this.rName = rName;
        this.rTime = rTime;
        this.status = status;
    }
}

class request extends Thread {
    String flightName;
    int flightWeight;
    int flightHalttime;
    String requestType;
    runway rw;

    public request(String flightName, int flightWeight, int flightHalttime, String ch, runway rw) {
        this.flightName = flightName;
        this.flightWeight = flightWeight;
        this.flightHalttime = flightHalttime;
        this.requestType = ch;
        this.rw = rw;
    }

    public void run() {
        rw.status = false;
        try {
            System.out.println("\t\t\t\t\t\t RUNWAY DETAILS");
            System.out.println("------------------------------------------------------------------------");
            System.out.println("\t" + requestType.toUpperCase() + " is Approved for " + flightName.toUpperCase() + " with " + flightWeight + " tons of weight in " + rw.rName);
            System.out.println("\tTouch down will happen in 15 sec.");
            System.out.println("\tThe Plane will stop after " + flightHalttime + " sec of touch down.");
            System.out.println("\t" + rw.rName + " will be ready for next request in " + flightHalttime + " sec.");
            System.out.println("------------------------------------------------------------------------\n");
            Thread.sleep(1000 * flightHalttime);
            rw.status = true;
        } catch (Exception e) {
            System.out.println("Exception occured in runway details");
        }
    }
}

public class ATC extends Thread {
    String name, ch;
    int weight, time;

    static Scanner sc = new Scanner(System.in);
    ArrayList<flight> flights = new ArrayList<flight>();
    ArrayList<runway> runways = new ArrayList<runway>();

    public ATC() {
        flight f1 = new flight("ATR", 12, 30);
        flight f2 = new flight("AIRBUS", 20, 40);
        flight f3 = new flight("BOEING", 40, 50);
        flight f4 = new flight("CARGO", 100, 60);
        flights.add(f1);
        flights.add(f2);
        flights.add(f3);
        flights.add(f4);

        runway r1 = new runway("Runway 1", 40, true);
        runway r2 = new runway("Runway 2", 60, true);
        runway r3 = new runway("Runway 3", 80, true);
        runway r4 = new runway("Runway 4", 90, true);
        runways.add(r1);
        runways.add(r2);
        runways.add(r3);
        runways.add(r4);
    }

    public static void main(String[] args) {
        int ans = 1;
        ATC a = new ATC();
        while (ans == 1) {
            a.mainMenu();
            System.out.println("\n\t\tDo you want to continue.......(yes-1 no-0)\n");
            ans = sc.nextInt();
            switch (ans) {
                case 0:
                    currentThread().stop();
                    break;
                case 1:
                    continue;
                default:
                    System.out.println("\t*-- Invalid Option --*");
                    break;
            }
        }
    }

    public void mainMenu() {
        System.out.println("\t\t\t\t\t\t\t      AIR TRAFFIC CONTROL     ");

        System.out.println("\t\t\t\t\t\t\t---------------------------------");
        System.out.println("\t\t\t\t\t\t\t|  1.Take Off Request           |");
        System.out.println("\t\t\t\t\t\t\t|  2.Landing Request            |");
        System.out.println("\t\t\t\t\t\t\t|  3.Emergency Landing Request  |");
        System.out.println("\t\t\t\t\t\t\t|  4.Show RunWays               |");
        System.out.println("\t\t\t\t\t\t\t|  5.Flight Details             |");
        System.out.println("\t\t\t\t\t\t\t|  6.Exit                       |");
        System.out.println("\t\t\t\t\t\t\t---------------------------------");

        System.out.println("Enter your choice: ");
        int option = sc.nextInt();
        switch (option) {
            case 1:
            case 2:
            case 3:
                sc.nextLine();
                getChoice(option);
                for (flight f : flights) {
                    if ((f.fName).equalsIgnoreCase(name)) {
                        time = f.computeTime(weight);
                        assignRunway();
                    }
                    if (!f.fName.equals(name)){
                        System.out.println("\tFlight name " + name + " not available");
                        currentThread().stop();
                        break;
                    }
                }
                break;
            case 4:
                runwayDetails();
                break;
            case 5:
                flightDetails();
                break;
            case 6:
                System.out.println("\n\t*-- Thank you for using the Application --*");
                currentThread().stop();
                break;
            default:
                System.out.println("\t*-- Invalid Option --*");
                currentThread().stop();
                break;
        }
    }

    public void getChoice(int option) {
        System.out.println("\nEnter the Flight Name: ");
        name = sc.next();
        for (flight f : flights) {
            if (f.fName.equalsIgnoreCase(name)){
                System.out.println("\nEnter the Weight of the Plane (Within " +  f.fWeight + " Tons): ");
                weight = sc.nextInt();
            }
            if (!(weight <= f.fWeight)){
                System.out.println("\tWeight not in range " + f.fName + " can carry only upto " + f.fWeight);
                currentThread().stop();
                break;
            }
        }
        if (option == 1) {
            ch = "take-off";
        } else if (option == 2) {
            ch = "landing";
        } else ch = "emergency landing";
    }

    public void assignRunway() {
        int full = 0;
        for (int i = 0; i < flights.size(); i++) {
            if (time <= runways.get(i).rTime && runways.get(i).status) {
                full = 1;
                request r = new request(name, weight, time, ch, runways.get(i));
                r.start();
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    System.out.println("Exception occured");
                }
                break;
            }
        }
        if (full == 0) {
            System.out.println("You have to Wait...");
        }
    }

    public void flightDetails() {
        System.out.println("\t\t\t\t\t\t\t      FLIGHT DETAILS     ");
        System.out.println("\t\t\t\t\t--------------------------------------------------");
        for (flight f : flights) {
            System.out.println("\t\t\t\t\t" + f.fName + " carry upto " + f.fWeight + " tons.Time to halt is " + f.timeHalt + " secs. ");
        }
        System.out.println("\t\t\t\t\t--------------------------------------------------");
    }

    public void runwayDetails() {
        System.out.println("\t\t\t\t\t\t\t      RUNWAY DETAILS     ");
        System.out.println("\t\t\t\t\t------------------------------------------");
        for (runway r : runways) {
            if (r.status) {
                System.out.println("\t\t\t\t\t\t\t" + r.rName + " is Free.");
            } else System.out.println("\t\t\t\t\t\t\t" + r.rName + " is Busy for " + r.rTime + " secs.");
        }
        System.out.println("\t\t\t\t\t------------------------------------------");
    }
    
}
