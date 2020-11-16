/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProblemRepresentation;

/**
 *
 * @author Renan
 */
public class Request implements Comparable, Cloneable {

    private Integer id;
    private Integer origin;
    private Integer destination;
    
    private Long pickupTimeWindowLower; 
    private Long pickupTimeWindowUpper; 
    private Long deliveryTimeWindowLower; 
    private Long deliveryTimeWindowUpper; 
    
    private Long pickupTime = (long) -1; 
    private Long deliveryTime = (long) -1;
    private Long timeWindowDefault = (long) 10;

    public Request() {

    }

    public Request(int id, int origin, int destination, long pickupTimeWindowLower, long deliveryTimeWindowLower) {
        this.id = id;

        this.origin = origin;
        this.destination = destination;

        this.pickupTimeWindowLower = pickupTimeWindowLower;
        this.pickupTimeWindowUpper = pickupTimeWindowLower + timeWindowDefault;

        this.deliveryTimeWindowLower = deliveryTimeWindowLower;
        this.deliveryTimeWindowUpper = deliveryTimeWindowLower + timeWindowDefault;

    }

    public Request(int id, int origin, int destination, long pickupE, long pickupL, long deliveryE, long deliveryL) {
        this.id = id;

        this.origin = origin;
        this.destination = destination;

        this.pickupTimeWindowLower = pickupE;
        if (pickupL - pickupE > 0) {
            this.pickupTimeWindowUpper = pickupL;
        } else {
            this.pickupTimeWindowUpper = pickupE + timeWindowDefault;
        }

        this.deliveryTimeWindowLower = deliveryE;
        if (deliveryL - deliveryE > 0) {
            this.deliveryTimeWindowUpper = deliveryL;
        } else {
            this.deliveryTimeWindowUpper = deliveryE + timeWindowDefault;
        }
    }

    public Request(Request req) {
        this.id = req.id;

        this.origin = req.origin;
        this.destination = req.destination;

        this.pickupTimeWindowLower = req.pickupTimeWindowLower;
        this.pickupTimeWindowUpper = req.pickupTimeWindowUpper;

        this.deliveryTimeWindowLower = req.deliveryTimeWindowLower;
        this.deliveryTimeWindowUpper = req.deliveryTimeWindowUpper;

        this.pickupTime = req.pickupTime;
        this.deliveryTime = req.deliveryTime;

        this.timeWindowDefault = req.timeWindowDefault;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public long getPickupTimeWindowLower() {
        return pickupTimeWindowLower;
    }

    public void setPickupTimeWindowLower(long pickupE) {
        this.pickupTimeWindowLower = pickupE;
    }

    public long getPickupTimeWindowUpper() {
        return pickupTimeWindowUpper;
    }

    public void setPickupTimeWindowUpper(long pickupTimeWindowUpper) {

        if (pickupTimeWindowUpper - this.pickupTimeWindowLower > 0) {
            this.pickupTimeWindowUpper = pickupTimeWindowUpper;
        } else {
            this.pickupTimeWindowUpper = pickupTimeWindowLower + timeWindowDefault;
        }
    }

    public long getDeliveryTimeWindowLower() {
        return deliveryTimeWindowLower;
    }

    public void setDeliveryTimeWindowLower(long deliveryTimeWindowLower) {
        this.deliveryTimeWindowLower = deliveryTimeWindowLower;
    }

    public long getDeliveryTimeWindowUpper() {
        return deliveryTimeWindowUpper;
    }

    public void setDeliveryTimeWindowUpper(long deliveryTimeWindowUpper) {

        if (deliveryTimeWindowUpper - this.deliveryTimeWindowLower > 0) {
            this.deliveryTimeWindowUpper = deliveryTimeWindowUpper;
        } else {
            this.deliveryTimeWindowUpper = deliveryTimeWindowLower + timeWindowDefault;
        }
    }

    public Long getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Long pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public long getTimeWindowDefault() {
        return timeWindowDefault;
    }

    public void setTimeWindowDefault(long timeWindowDefault) {
        if (timeWindowDefault > 0) {
            this.timeWindowDefault = timeWindowDefault;
        }
    }

    public String getStringToFile(){
        String string = id + "\t" + origin + "\t" + destination + "\t" + pickupTimeWindowLower + "\t" +pickupTimeWindowUpper 
                + "\t" + deliveryTimeWindowLower + "\t" + deliveryTimeWindowUpper;
        return string;
    }
    
    @Override
    public String toString() {

        if (this.origin == null || this.destination == null || this.pickupTimeWindowLower == null || this.pickupTimeWindowUpper == null || this.deliveryTimeWindowLower == null || this.deliveryTimeWindowUpper == null) {
            return "V" + origin + "-V" + destination + ": [" + pickupTimeWindowLower + "," + pickupTimeWindowUpper + "]-[" + deliveryTimeWindowLower + "," + deliveryTimeWindowUpper + "];";
        }

        String s = "id_" + id + "_V" + origin + "-V" + destination + ": [";

        if (pickupTime / 60 > -1 && pickupTime / 60 < 10) {
            s += "0";
        }
        s += pickupTime / 60 + "h";

        if (pickupTime % 60 > -1 && pickupTime % 60 < 10) {
            s += "0";
        }
        s += pickupTime % 60 + ": ";

        if (pickupTimeWindowLower / 60 > -1 && pickupTimeWindowLower / 60 < 10) {
            s += "0";
        }
        s += pickupTimeWindowLower / 60 + "h";

        if (pickupTimeWindowLower % 60 > -1 && pickupTimeWindowLower % 60 < 10) {
            s += "0";
        }
        s += pickupTimeWindowLower % 60 + ",";

        if (pickupTimeWindowUpper / 60 > -1 && pickupTimeWindowUpper / 60 < 10) {
            s += "0";
        }
        s += pickupTimeWindowUpper / 60 + "h";

        if (pickupTimeWindowUpper % 60 > -1 && pickupTimeWindowUpper % 60 < 10) {
            s += "0";
        }
        s += pickupTimeWindowUpper % 60 + "]-[";

        if (deliveryTime / 60 > -1 && deliveryTime / 60 < 10) {
            s += "0";
        }
        s += deliveryTime / 60 + "h";

        if (deliveryTime % 60 > -1 && deliveryTime % 60 < 10) {
            s += "0";
        }
        s += deliveryTime % 60 + ": ";

        if (deliveryTimeWindowLower / 60 > -1 && deliveryTimeWindowLower / 60 < 10) {
            s += "0";
        }
        s += deliveryTimeWindowLower / 60 + "h";

        if (deliveryTimeWindowLower % 60 > -1 && deliveryTimeWindowLower % 60 < 10) {
            s += "0";
        }
        s += deliveryTimeWindowLower % 60 + ",";

        if (deliveryTimeWindowUpper / 60 > -1 && deliveryTimeWindowUpper / 60 < 10) {
            s += "0";
        }
        s += deliveryTimeWindowUpper / 60 + "h";

        if (deliveryTimeWindowUpper % 60 > -1 && deliveryTimeWindowUpper % 60 < 10) {
            s += "0";
        }
        s += deliveryTimeWindowUpper % 60 + "] ";

        //FALTA ACRESCENTAR O PICKUPTIME E O DELIVERYTIME NA IMPRESSAO
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Request && equals((Request) obj);
    }

    public boolean equals(Request request2) {
        if (this == request2) {
            return true;
        }

        if (request2 == null) {
            return false;
        }

        if (!id.equals(request2.id) /*|| !origin.equals(request2.origin)  || !destination.equals(request2.destination) || 
				!pickupE.equals(request2.pickupE) || !pickupL.equals(request2.pickupL) || 
				!deliveryE.equals(request2.deliveryE) || !deliveryL.equals(request2.deliveryL)*/ /*||
				!pickupTime.equals(request2.pickupTime) || !deliveryTime.equals(request2.deliveryTime) ||
				!timeWindowDefault.equals(request2.timeWindowDefault)*/) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {

        if (origin == -1 || destination == -1) {
            return -1;
        }

        int hash = 0;

        String s = Integer.toString(id) + Integer.toString(origin) + Integer.toString(destination)
                + Long.toString(pickupTimeWindowLower) + Long.toString(pickupTimeWindowUpper)
                + Long.toString(deliveryTimeWindowLower) + Long.toString(deliveryTimeWindowUpper)
                + Long.toString(pickupTime) + Long.toString(deliveryTime) + Long.toString(timeWindowDefault);

        hash = s.hashCode();

        return hash;
    }

    @Override
    public int compareTo(Object obj) {
        return compareTo((Request) obj);
    }

    public int compareTo(Request r2) {
        if (r2.getId().compareTo(this.id) == 0) {
            return 0;
        }

        if (((Long) r2.pickupTimeWindowLower).compareTo(this.pickupTimeWindowLower) > 0) {
            return -1;
        }

        if (((Long) r2.pickupTimeWindowLower).compareTo(this.pickupTimeWindowLower) < 0) {
            return 1;
        }

        if (((Long) r2.deliveryTimeWindowLower).compareTo(this.deliveryTimeWindowLower) > 0) {
            return -1;
        }

        if (((Long) r2.deliveryTimeWindowLower).compareTo(this.deliveryTimeWindowLower) < 0) {
            return 1;
        }

        if (((Long) r2.pickupTimeWindowUpper).compareTo(this.pickupTimeWindowUpper) > 0) {
            return -1;
        }

        if (((Long) r2.pickupTimeWindowUpper).compareTo(this.pickupTimeWindowUpper) < 0) {
            return 1;
        }

        if (((Long) r2.deliveryTimeWindowUpper).compareTo(this.deliveryTimeWindowUpper) > 0) {
            return -1;
        }

        if (((Long) r2.deliveryTimeWindowUpper).compareTo(this.deliveryTimeWindowUpper) < 0) {
            return 1;
        }

        return 0;
    }

    @Override
    public Object clone() {

        Request request = null;

        try {
            request = (Request) super.clone();

            request.id = new Integer(this.id);

            request.origin = new Integer(this.origin);

            request.destination = new Integer(this.destination);

            request.pickupTimeWindowLower = new Long(this.pickupTimeWindowLower);

            request.pickupTimeWindowUpper = new Long(this.pickupTimeWindowUpper);

            request.deliveryTimeWindowLower = new Long(this.deliveryTimeWindowLower);

            request.deliveryTimeWindowUpper = new Long(this.deliveryTimeWindowUpper);

            request.pickupTime = new Long(this.pickupTime);

            request.deliveryTime = new Long(this.deliveryTime);

            request.timeWindowDefault = new Long(this.timeWindowDefault);

        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return request;

    }

}
