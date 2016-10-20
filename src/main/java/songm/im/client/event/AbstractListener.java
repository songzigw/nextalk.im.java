package songm.im.client.event;

public abstract class AbstractListener<T> implements ActionListener<T> {

    @Override
    public Long getSequence() {
        return null;
    }

}
