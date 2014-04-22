import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by root on 4/22/14.
 */

public class Main {
    public static int getMessageCountInput(GMailReader gmail, int messageCount) {
        boolean outOfRange = true;
        int res = 0;
        if (gmail.getNewMessageCount() != 0)
            while (outOfRange) {
                System.out.println("Number of messages to show: ");
                Scanner in = new Scanner(System.in);
                messageCount = in.nextInt();

                if (messageCount <= gmail.getNewMessageCount()) {
                    outOfRange = false;
                    res =  messageCount;
                }
                else System.out.println("There are less than " + messageCount + " new messages!");
            }
        return res;
    }


    public static void fetchMessages(GMailReader gmail, int messageCount) {
        for (int i = 1; i <= messageCount; i++) {
            try {
                ArrayList<String> message = gmail.getMessage(i);
                System.out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String[] getUserInfo() {
        Scanner in = new Scanner(System.in);
        String[] userInfo = new String[2];

        boolean isEmpty = true;
        while (isEmpty) {
            System.out.println("Enter email: ");
            userInfo[0] = in.nextLine();

            if (userInfo[0].equals("")) {
                System.out.println("Email not entered. Try again.");
            }
            else if (!userInfo[0].endsWith("@gmail.com"))
                System.out.println("This is not a GMail account! Try again.");
            else isEmpty = false;
        }

        isEmpty = true;
        while (isEmpty) {
            System.out.println("Enter password: ");
            userInfo[1] = in.nextLine();

            if (userInfo[1].equals("")) {
                System.out.println("Pasword not entered. Try again.");
            } else isEmpty = false;
        }
        return userInfo;
    }
    public static void main (String args[]) {
        int messageCount = 10; //By default returns 10 newest unread messages

        GMailReader gmail = new GMailReader();
        gmail.connect("pop.gmail.com");

        String[] userInfo = getUserInfo();
        gmail.login(userInfo[0], userInfo[1]);

        System.out.println("New message: " + gmail.getNewMessageCount());
        messageCount = getMessageCountInput(gmail, messageCount);
        fetchMessages(gmail, messageCount);

        gmail.logout();
        gmail.disconnect();
    }

}
