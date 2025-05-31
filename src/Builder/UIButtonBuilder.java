package Builder;

import javax.swing.*;
import java.awt.event.ActionListener;

public class UIButtonBuilder {
    private final JButton button;

    public UIButtonBuilder(String text) {
        this.button = new JButton(text);
    }

    public UIButtonBuilder setBounds(int x, int y, int width, int height) {
        button.setBounds(x, y, width, height);
        return this;
    }

    public UIButtonBuilder addActionListener(ActionListener listener) {
        button.addActionListener(listener);
        return this;
    }

    public JButton build() {
        return button;
    }
}
