import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.vlad.ejb.ManagerLicitatie;

public class DBUpdater {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        ManagerLicitatie bean = managerLicitatieBeanLookup("java:global/final-main/final-ejb/ManagerLicitatieImpl!com.vlad.ejb.ManagerLicitatie");
        UpdateLicitatieStatusTask task = new UpdateLicitatieStatusTask(bean);
        ScheduledFuture<?> urmaresteLicitatieHandler = scheduler.scheduleAtFixedRate(task, 0, 60, TimeUnit.SECONDS);
    }

    private static ManagerLicitatie managerLicitatieBeanLookup(String name) {
        ManagerLicitatie bean = null;
        try {
            InitialContext ctx = new InitialContext();
            Object obj = ctx.lookup(name);
            bean = (ManagerLicitatie) obj;
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return bean;
    }

}
