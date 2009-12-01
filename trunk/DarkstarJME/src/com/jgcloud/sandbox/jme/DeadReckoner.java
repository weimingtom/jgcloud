package com.jgcloud.sandbox.jme;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import java.util.logging.Logger;

/**
 * A utility class that is going to interpolate a remote players estimated
 * location based on a start and end point over a given amount of time. The
 * server will be responsible for telling us where the remote player will be in
 * the future, and at what point (on the system clock) they're going to be
 * there. Using this information, you can call the getCurrentTranslation()
 * and
 * @author Richard
 */
public class DeadReckoner {

    private Vector3f startTranslation, finishTranslation;
    private Quaternion startRotation, finishRotation;
    private long startTimeMillis, finishTimeMillis;
    private Logger logger = Logger.getLogger(DeadReckoner.class.getName());

    public Vector3f getCurrentTranslation() {
        long currentTimeMillis = System.currentTimeMillis();

        if (currentTimeMillis > finishTimeMillis) {

            return finishTranslation;
        } else {
            float percent = ((float)(currentTimeMillis-startTimeMillis)) / ((float)(finishTimeMillis-startTimeMillis));
            Vector3f interpolatedTranslation = new Vector3f();
            interpolatedTranslation.interpolate(startTranslation, finishTranslation, percent);
            return interpolatedTranslation;
        }
    }


    public Quaternion getCurrentRotation() {
        long currentTimeMillis = System.currentTimeMillis();

        if (currentTimeMillis > finishTimeMillis) {
            return finishRotation;
        } else {
            float percent = ((float)(currentTimeMillis-startTimeMillis)) / ((float)(finishTimeMillis-startTimeMillis));
            Quaternion interpolatedRotation = new Quaternion();
            
            // Not sure if slerp is right, but you never know !
            interpolatedRotation.slerp(startRotation, finishRotation, percent);
            return interpolatedRotation;
        }
    }

    /**
     * @return the startTranslation
     */
    public Vector3f getStartTranslation() {
        return startTranslation;
    }

    /**
     * @param startTranslation the startTranslation to set
     */
    public void setStartTranslation(Vector3f startTranslation) {
        this.startTranslation = startTranslation;
    }

    /**
     * @return the finishTranslation
     */
    public Vector3f getFinishTranslation() {
        return finishTranslation;
    }

    /**
     * @param finishTranslation the finishTranslation to set
     */
    public void setFinishTranslation(Vector3f finishTranslation) {
        this.finishTranslation = finishTranslation;
    }

    /**
     * @return the startRotation
     */
    public Quaternion getStartRotation() {
        return startRotation;
    }

    /**
     * @param startRotation the startRotation to set
     */
    public void setStartRotation(Quaternion startRotation) {
        this.startRotation = startRotation;
    }

    /**
     * @return the finishRotation
     */
    public Quaternion getFinishRotation() {
        return finishRotation;
    }

    /**
     * @param finishRotation the finishRotation to set
     */
    public void setFinishRotation(Quaternion finishRotation) {
        this.finishRotation = finishRotation;
    }

    /**
     * @return the startTimeMillis
     */
    public long getStartTimeMilis() {
        return startTimeMillis;
    }

    /**
     * @param startTimeMillis the startTimeMillis to set
     */
    public void setStartTimeMilis(long startTimeMilis) {
        this.startTimeMillis = startTimeMilis;
    }

    /**
     * @return the finishTimeMillis
     */
    public long getFinishTimeMilis() {
        return finishTimeMillis;
    }

    /**
     * @param finishTimeMillis the finishTimeMillis to set
     */
    public void setFinishTimeMilis(long finishTimeMilis) {
        this.finishTimeMillis = finishTimeMilis;
    }

}
