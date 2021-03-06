package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }

    public BankClient getClientById(long id) throws DBException {
        try {
            return getBankClientDAO().getClientById(id);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public BankClient getClientByName(String name) {

        BankClientDAO dao = getBankClientDAO();
        return dao.getClientByName(name);
    }

    public List<BankClient> getAllClient() {
        BankClientDAO dao = getBankClientDAO();
        return  dao.getAllBankClient();
    }

    public boolean deleteClient(String name) {

        BankClientDAO dao = getBankClientDAO();
        return dao.removeClient(name);
    }

    public boolean addClient(BankClient client) throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.addClient(client);
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return true;
    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) {

        BankClientDAO dao = getBankClientDAO();
        if (sender.getMoney() >= value){

                BankClient client = dao.getClientByName(name);
                if (client != null) {
                    dao.updateClientsMoney(sender.getName(), sender.getPassword(), -value);
                    dao.updateClientsMoney(client.getName(), client.getPassword(),  value);
                    return true;
                } else {
                    System.out.println("net takogo clienta");
                    return false;
                }

        }
        return false;
    }

    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
    public void createTable() throws DBException{
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("db_example?").          //db name
                    append("user=root&").          //login
                    append("password=root");       //password

            System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {

        return new BankClientDAO(getMysqlConnection());
    }
}
