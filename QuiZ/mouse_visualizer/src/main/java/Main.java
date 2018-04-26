import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;

import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;

public class Main {
    private static boolean run = true;
    public static void main(String[] args) {
        // might throw a UnsatisfiedLinkError if the native library fails to load or a RuntimeException if hooking fails
        GlobalMouseHook mouseHook = new GlobalMouseHook(); // add true to the constructor, to switch to raw input mode

        PrintWriter writer = null;
        try {
            writer = new PrintWriter("mouse-movement.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println("Global mouse hook successfully started, press [middle] mouse button to shutdown. Connected mice:");
        for(Entry<Long,String> mouse:GlobalMouseHook.listMice().entrySet())
            System.out.format("%d: %s\n", mouse.getKey(), mouse.getValue());

        final PrintWriter finalWriter = writer;
        mouseHook.addMouseListener(new GlobalMouseAdapter() {
            @Override public void mousePressed(GlobalMouseEvent event)  {
                System.out.println(event);
                if((event.getButtons()&GlobalMouseEvent.BUTTON_LEFT)!=GlobalMouseEvent.BUTTON_NO
                        && (event.getButtons()&GlobalMouseEvent.BUTTON_RIGHT)!=GlobalMouseEvent.BUTTON_NO)
                    System.out.println("Both mouse buttons are currenlty pressed!");
                if(event.getButton()==GlobalMouseEvent.BUTTON_MIDDLE);
                    //run = false;
            }
            @Override public void mouseReleased(GlobalMouseEvent event)  {
                System.out.println(event);
                finalWriter.println(event);
            }
            @Override public void mouseMoved(GlobalMouseEvent event) {
                System.out.println(event);
                finalWriter.println(event);
            }
            @Override public void mouseWheel(GlobalMouseEvent event) {
                System.out.println(event);
                finalWriter.println(event);
            }
        });

        try {
            while(run) Thread.sleep(128);
        } catch(InterruptedException e) { /* nothing to do here */ }
        finally {
            writer.close();
            mouseHook.shutdownHook(); }
    }
}



