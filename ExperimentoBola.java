import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.animation.Animation;
import java.util.Random;
import javafx.scene.input.KeyCode;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import javafx.scene.shape.Shape;



public class ExperimentoBola extends Application
{
    private int velocidadEnX;
    private int velocidadEnY;
    private int velocidadPlataforma;
    private static int RADIO = 20;
    private int tiempoEnSegundos;
    private ArrayList<Rectangle> ladrillos;
    private static final int NUMERO_LADRILLOS = 4;
    private static final int ANCHO_LADRILLO = 40;
    private static final int ALTO_LADRILLO = 10;
    private static final int ALTO_BARRA_SUPERIOR = 20;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage escenario)
    {
        Group contenedor = new Group();

        velocidadEnX = 1;
        velocidadEnY = 1;
        tiempoEnSegundos = 0;
        
        Scene escena = new Scene(contenedor, 500, 500);
        escenario.setScene(escena);

        Circle circulo = new Circle();
        circulo.setFill(Color.RED);  
        circulo.setRadius(RADIO);

        Rectangle plataforma = new Rectangle();
        plataforma.setWidth(50);
        plataforma.setHeight(5);
        plataforma.setTranslateX(225);
        plataforma.setTranslateY(480);
        plataforma.setFill(Color.BLUE);
        contenedor.getChildren().add(plataforma);
        
        Random aleatorio = new Random();
        
        ladrillos = new ArrayList<>();
        
        
        int numeroLadrillosAnadidos = 0;
        while (numeroLadrillosAnadidos < NUMERO_LADRILLOS) {
            boolean encontradoLadrillo = false;
            while (!encontradoLadrillo) {
                int posXLadrillo = aleatorio.nextInt(((int)escena.getWidth() - ANCHO_LADRILLO) + ANCHO_LADRILLO);
                int posYLadrillo = aleatorio.nextInt(((int)escena.getWidth() / 2) - ANCHO_LADRILLO + ANCHO_LADRILLO);
                Rectangle posibleLadrillo = new Rectangle();
                posibleLadrillo.setWidth(ANCHO_LADRILLO);
                posibleLadrillo.setHeight(ALTO_LADRILLO);
                posibleLadrillo.setFill(Color.GREEN);
                posibleLadrillo.setTranslateX(posXLadrillo);
                posibleLadrillo.setTranslateY(posYLadrillo);
                //comprobar si el ladrillo es valido
                boolean solapamientoDetectado = false;
                int ladrilloActual = 0;
                while (ladrilloActual < ladrillos.size() && !solapamientoDetectado) {
                    Shape interseccion = Shape.intersect(posibleLadrillo, ladrillos.get(ladrilloActual));
                    if (interseccion.getBoundsInParent().getWidth() != -1) {
                        solapamientoDetectado = true;
                    }
                    ladrilloActual++;
                }
                //encontrado ladrillo valido
                if (!solapamientoDetectado) {
                    encontradoLadrillo = true;
                    ladrillos.add(posibleLadrillo);
                    contenedor.getChildren().add(posibleLadrillo);
                }
            }
            numeroLadrillosAnadidos++;
        }
        
        
        //for (int contador = 0; contador < NUMERO_LADRILLOS ; contador++) {
           // Rectangle ladrillo = new Rectangle();
           // ladrillo.setWidth(40);
           // ladrillo.setHeight(10);
           // ladrillo.setFill(Color.GREEN);
           // ladrillos.add(ladrillo);
        //}
        
        //boolean seInterceptan = true;
        //for (int contador = 0; contador < ladrillos.size(); contador++) {
        //    ladrillos.get(contador).setTranslateX(20 + aleatorio.nextInt(500 - 40));
        //    ladrillos.get(contador).setTranslateY(aleatorio.nextInt(250));
        //    contenedor.getChildren().add(ladrillos.get(contador));
        //    for (int contador2 = 0; contador2 < contador; contador2++) {
        //        if (contador != contador2) {
        //            seInterceptan = true;   
        //            while (seInterceptan) {
        //                Shape comprobador = Shape.intersect(ladrillos.get(contador), ladrillos.get(contador2));
        //                double ancho = comprobador.getBoundsInParent().getWidth();
        //                if (ancho == -1.00) {
        //                    seInterceptan = false;
        //                }
        //                else {
        //                    ladrillos.get(contador).setTranslateX(aleatorio.nextInt(20 + aleatorio.nextInt(500 - 40)));
        //                    ladrillos.get(contador).setTranslateY(aleatorio.nextInt(250));
        //                }
        //           }
        //        }
        //    }
        //}

        velocidadPlataforma = 1;

        circulo.setCenterX(20 + aleatorio.nextInt(500 - 40));
        circulo.setCenterY(50);
        contenedor.getChildren().add(circulo);
        
        Label tiempoPasado = new Label("0");
        contenedor.getChildren().add(tiempoPasado);

        
        escenario.show();

        Timeline timeline = new Timeline();
        KeyFrame keyframe = new KeyFrame(Duration.seconds(0.01), event -> {

                        // Controlamos si la bola rebota a ziquierda o derecha
                        if (circulo.getBoundsInParent().getMinX() <= 0 ||
                        circulo.getBoundsInParent().getMaxX() >= escena.getWidth()) {
                            velocidadEnX = -velocidadEnX;                              
                        }

                        // Conrolamos si la bola rebota arriba y abajo
                        if (circulo.getBoundsInParent().getMinY() <= 0) {
                            velocidadEnY = -velocidadEnY;
                        }

                        if (circulo.getBoundsInParent().getMaxY() == plataforma.getBoundsInParent().getMinY()) {
                            double centroEnXDeLaBola = circulo.getBoundsInParent().getMinX() + RADIO;
                            double minEnXDeLaPlataforma = plataforma.getBoundsInParent().getMinX();
                            double maxEnXDeLaPlataforma = plataforma.getBoundsInParent().getMaxX();
                            if ((centroEnXDeLaBola >= minEnXDeLaPlataforma) &&
                            (centroEnXDeLaBola <= maxEnXDeLaPlataforma)) {
                                //La bola esta sobre la plataforma
                                velocidadEnY = -velocidadEnY;
                            }
                        }

                        circulo.setTranslateX(circulo.getTranslateX() + velocidadEnX);
                        circulo.setTranslateY(circulo.getTranslateY() + velocidadEnY);

                        plataforma.setTranslateX(plataforma.getTranslateX() + velocidadPlataforma);
                        if (plataforma.getBoundsInParent().getMinX() == 0  || 
                        plataforma.getBoundsInParent().getMaxX() == escena.getWidth()) {
                            velocidadPlataforma = 0;
                        }
                        
                        
                        for (int i = 0; i < ladrillos.size(); i++) {
                            Rectangle ladrilloAComparar = ladrillos.get(i);
                            Shape interseccion = Shape.intersect(circulo, ladrilloAComparar);
                            if (interseccion.getBoundsInParent().getWidth() != -1) {
                                contenedor.getChildren().remove(ladrilloAComparar);
                                ladrillos.remove(ladrilloAComparar);
                                i--;
                            }
                        }
                        
                        
                        
                        // Actualizamos la etiqueta del tiempo
                        int minutos = tiempoEnSegundos / 60;
                        int segundos = tiempoEnSegundos % 60;
                        tiempoPasado.setText(minutos + ":" + segundos);                        

                        // Comrpobamos si el juego debe detenerse
                        if (circulo.getBoundsInParent().getMinY() > escena.getHeight()) {
                            Label mensajeGameOver = new Label("Game over");
                            mensajeGameOver.setTranslateX(escena.getWidth() / 2);
                            mensajeGameOver.setTranslateY(escena.getHeight() / 2);
                            contenedor.getChildren().add(mensajeGameOver);
                            timeline.stop();
                        }
                        
                    });  
        timeline.getKeyFrames().add(keyframe);

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();     

        escena.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.RIGHT && 
                plataforma.getBoundsInParent().getMaxX() != escena.getWidth()) {
                    velocidadPlataforma = 1;
                }
                else if (event.getCode() == KeyCode.LEFT && 
                plataforma.getBoundsInParent().getMinX() != 0) {
                    velocidadPlataforma = -1;
                }
            });

        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                tiempoEnSegundos++;
            }                        
        };
        Timer timer = new Timer();
        timer.schedule(tarea, 0, 1000);
            
    }

}





