package com.sensei.twoplayer.core.ui ;

import java.awt.* ;
import java.awt.event.* ;

@SuppressWarnings( "serial" )
public class MessageDialog extends Dialog implements ActionListener {
    
    protected MessageDialog( String message ) {
        super( new Frame(), "Message" ) ;
        setUpGUI( message ) ;
        setModal( true ) ;
    }

    public void actionPerformed( ActionEvent e ) {
        setVisible( false ) ;
        dispose() ;
    }

    private void setUpGUI( String message ) {
        setLayout( new BorderLayout() ) ;
        add( new Label( message ) ) ;
        Button ok = new Button( "OK" ) ;
        ok.addActionListener( this ) ;
        Panel tempPanel = new Panel() ;
        tempPanel.add( ok ) ;
        add( tempPanel, "South" ) ;
        setSize( 250, 150 ) ;
    }

    public static void showMessageDialog( String message ) {
        MessageDialog dialog = new MessageDialog( message ) ;
        dialog.setVisible( true ) ;
    }
}
