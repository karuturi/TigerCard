import com.rajani.tigercard.model.Zone;
import com.rajani.tigercard.request.Ticket;
import com.rajani.tigercard.request.TigerCardRequest;
import com.rajani.tigercard.service.TigerCardService;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TigerCardApplication {

    TigerCardService tigerCardService;

    public TigerCardApplication() {
        this.tigerCardService = new TigerCardService();
    }

    /**
     * sample input:
     * 2
     * 2007-12-01T10:15:30, ONE, ONE
     * 2007-12-01T04:15:30, ONE, ONE
     */
    public static void main(String[] args) {
        //construct request from command line
        //pass to service and print result
        List<String> input = new LinkedList<>();
        Scanner in = new Scanner(System.in);
        int size = in.nextInt();
        in.nextLine();
        for (int i = 0; i < size; i++) {
            input.add(in.nextLine());
        }
        in.close();

        TigerCardApplication app = new TigerCardApplication();
        System.out.println(app.process(input));
    }

    public int process(List<String> input) {
        List<Ticket> tickets = new LinkedList<>();
        for (String in : input) {
            String[] ticketString = in.split(",");
            tickets.add(new Ticket(LocalDateTime.parse(ticketString[0].trim()), Zone.valueOf(ticketString[1].trim()),
                    Zone.valueOf(ticketString[2].trim())));
        }
        TigerCardRequest request = new TigerCardRequest(tickets);
        return tigerCardService.computeFare(request);
    }
}
