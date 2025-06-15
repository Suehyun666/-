package dialog;

public interface DialogResult<T> {
    boolean isConfirmed();
    T getResult();

    static <T> DialogResult<T> cancelled() {
        return new DialogResult<T>() {
            @Override
            public boolean isConfirmed() {
                return false;
            }

            @Override
            public T getResult() {
                return null;
            }
        };
    }

    static <T> DialogResult<T> confirmed(T result) {
        return new DialogResult<T>() {
            @Override
            public boolean isConfirmed() {
                return true;
            }

            @Override
            public T getResult() {
                return result;
            }
        };
    }
}
