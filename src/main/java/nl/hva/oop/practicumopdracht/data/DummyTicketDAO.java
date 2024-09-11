package nl.hva.oop.practicumopdracht.data;

import nl.hva.oop.practicumopdracht.models.Ticket;

import java.time.LocalDate;

import static nl.hva.oop.practicumopdracht.MainApplication.getDateTimeFormatter;
import static nl.hva.oop.practicumopdracht.MainApplication.getPersonDAO;

/**
 * DummyTicketDAO - DummyDetailDAO
 * This is a dummy DAO class that is used to test the application.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public class DummyTicketDAO extends TicketDAO {
    @Override
    public boolean load() {
        // belongsTo, destination, startDate, endDate, cost, checkedIn, description
        tickets.add(new Ticket(getPersonDAO().getById(0), "Ankara, Turkije",
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true,
                "Vlucht van Amsterdam naar Ankara. Vluchtcode TK 1234"));

        tickets.add(new Ticket(getPersonDAO().getById(0), "İzmir, Turkije",
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true,
                "Vlucht van Amsterdam naar İzmir. Vluchtcode TK 6479"));

        tickets.add(new Ticket(getPersonDAO().getById(0), "Amaroo, Australië",
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true,
                "Vlucht van Amsterdam naar Amaroo. Vluchtcode TK 8542"));

        tickets.add(new Ticket(getPersonDAO().getById(1), "Tenerife, Spanje",
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true,
                "Vlucht van Amsterdam naar Tenerife. Vluchtcode TK 6433"));

        tickets.add(new Ticket(getPersonDAO().getById(2), "Madrid, Spanje",
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true,
                "Vlucht van Amsterdam naar Madrid. Vluchtcode TK 6354"));

        tickets.add(new Ticket(getPersonDAO().getById(3), "Barcelona, Spanje",
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true,
                "Vlucht van Amsterdam naar Barcelona. Vluchtcode TK 3524"));

        tickets.add(new Ticket(getPersonDAO().getById(4), "Marrakesh, Marokko",
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true,
                "Vlucht van Amsterdam naar Marrakesh. Vluchtcode TK 4534"));

        tickets.add(new Ticket(getPersonDAO().getById(5), "Rabat, Marokko",
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true,
                "Vlucht van Amsterdam naar Amaroo. Vluchtcode TK 1234"));

        tickets.add(new Ticket(getPersonDAO().getById(6), "Istanboel, Turkije",
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true,
                "Vlucht van Amsterdam naar Amaroo. Vluchtcode TK 1234"));

        return true;
    }

    @Override
    public boolean save() {
        return false;
    }
}