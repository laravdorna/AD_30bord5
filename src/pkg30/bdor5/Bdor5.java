/* COMANDOS
 lanzar servidor oracle para trabajar desde java con el listener

 . oraenv
 orcl
 rlwrap sqlplus sys/oracle as sysdba 
 startup
 conn hr/hr
 exit
 lsnrctl start
 lsnrctl status

 */
/* añadir ojdbc7 a libraries*/
package pkg30.bdor5;

import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oracle
 */
public class Bdor5 {

    public static Connection conexion = null;

    public static Connection getConexion() throws SQLException {
        String usuario = "hr";
        String password = "hr";
        String host = "localhost";
        String puerto = "1521";
        String sid = "orcl";
        String ulrjdbc = "jdbc:oracle:thin:" + usuario + "/" + password + "@" + host + ":" + puerto + ":" + sid;

        conexion = DriverManager.getConnection(ulrjdbc);
        return conexion;
    }

    public static void closeConexion() throws SQLException {
        conexion.close();
    }

    public static void main(String[] args) throws SQLException {
        //Creamos objeto de la Clase que tiene los métodos para trabajar con ella:
        Bdor5 obj = new Bdor5();

        obj.getConexion();

        // obj.InsertarLineaPedido(4001, 48, 20, 10, 2004);
        // obj.InsertarLineaPedido(4001, 49, 20, 10, 2004);
        //obj.modificarNome("Alvaro Luna", 5);
        // obj.modificarNome("Lara", 5);
        obj.borrarLinea(4001, 48);
// select cursor (select a.linum, deref(a.item).itemnum, a.cantidad, a.descuento from table (b.pedido) a) from pedido_tab b where b.ordnum = 4001;

        obj.closeConexion();
    }

    /*
     a)metodo insireLinea que insira unha linea de pedido pasandolle 
     como minimo os seguintes datos :
     ordnum, linum,item,cantidad,descuent
     probar a inserir unha linea de pedido para o pedido de numero de orden 4001 :
     linum: 48
     item : 2004
     cantidad: 20
     descuento: 10
    
    
    
     */
    public void InsertarLineaPedido(int orden, int linum, int cantidad, int descuento, int item) {

        try {

            String sql = "insert into the (select p.pedido from pedido_tab p where p.ordnum =?) "
                    + "select ?,ref(s),?,? from item_tab s where s.itemnum=?";

            PreparedStatement stm = conexion.prepareStatement(sql);

            stm.setInt(1, orden);
            stm.setInt(2, linum);
            stm.setInt(3, cantidad);
            stm.setInt(4, descuento);
            stm.setInt(5, item);
            stm.executeUpdate();

            /*
             INSERT INTO THE (SELECT P.pedido FROM pedido_tab P WHERE P.ordnum = 1001) SELECT
             01, REF(S), 12, 0 FROM item_tab S WHERE S.itemnum = 1534;
             */
            System.out.println("Insertado !");

        } catch (SQLException ex) {
            Logger.getLogger(Bdor5.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en la insercción");

        }
    }
    /*
     b)metodo modificaLinea  que modifique o nome dun cliente pasandolle 
     como minimo o numero do cliente. 
     probar a modificar o nome del cliente 5 para que pase a chamarse'Alvaro Luna'
     */

    public void modificarNome(String nome, int numCliente) {

        try {

            String sql = "update cliente_tab set clinomb=? where clinum=?";
            PreparedStatement stm = conexion.prepareStatement(sql);

            stm.setString(1, nome);
            stm.setInt(2, numCliente);
            stm.executeUpdate();

            System.out.println("Nome modificado!");

        } catch (SQLException ex) {
            Logger.getLogger(Bdor5.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*
     c) metodo borraLinea que pasandolle como minimo o numero de orde dun pedido
     e un numero de linea, borre dita linea de dito pedido  
     probar a borrar a linea de pedido (linum)  48 do pedido cuxo ordnum e igual a 4001 
     */
    public void borrarLinea(int orden, int numlin) {

        try {
            String sql = "delete from the (select b.pedido from pedido_tab b where b.ordnum =?) where linum = ?";

            //delete from the (select b.pedido from pedido_tab b where b.ordnum =4001) where linum = 49;
            PreparedStatement stm = conexion.prepareStatement(sql);
            stm.setInt(1, orden);
            stm.setInt(2, numlin);
            stm.executeUpdate();

            System.out.println("Linea de pedido borrada!");

        } catch (SQLException ex) {
            Logger.getLogger(Bdor5.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
