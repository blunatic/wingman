package TankGame;
import java.awt.event.KeyEvent;
import java.util.Observable;

/**
 * Sets values for any Observable events (like KeyControl(s) )
 */
public class GameEvents extends Observable {
       int type;
       Object event;
       
   public void setValue(KeyEvent e) {
          type = 1; // let's assume this means key input. 
          //Should use CONSTANT value for this when you program
          event = e;
          setChanged();
         // trigger notification
         notifyObservers(this);  
   }

   public void setValue(String msg) {
          type = 2; 
          event = msg;
          setChanged();
         // trigger notification
         notifyObservers(this);  
        }
    }