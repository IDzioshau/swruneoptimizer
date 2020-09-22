package by.dobrush.swruneoptimizer.FXUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AutoCompleteBox {

    public interface AutoCompleteComparator<T> {
        boolean matches(String typedText, T objectToCompare);
    }

    public static<T> void autoCompleteComboBoxPlus(ComboBox<T> comboBox, AutoCompleteComparator<T> comparatorMethod) {
        ComboBox<T> comboBox1 = comboBox;
        ObservableList<T> data = comboBox1.getItems();
        comboBox1.setEditable(true);
        comboBox1.getEditor().focusedProperty().addListener(observable -> {
            if (comboBox1.getSelectionModel().getSelectedIndex() < 0) {
                comboBox1.getEditor().setText(null);
            }
        });
        comboBox1.addEventHandler(KeyEvent.KEY_PRESSED, t -> comboBox1.hide());
        comboBox1.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            private boolean moveCaretToPos = false;
            private int caretPos;

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP) {
                    caretPos = -1;
                    if (comboBox1.getEditor().getText() != null) {
                        moveCaret(comboBox1.getEditor().getText().length());
                    }
                    return;
                } else if (event.getCode() == KeyCode.DOWN) {
                    if (!comboBox1.isShowing()) {
                        comboBox1.show();
                    }
                    caretPos = -1;
                    if (comboBox1.getEditor().getText() != null) {
                        moveCaret(comboBox1.getEditor().getText().length());
                    }
                    return;
                } else if (event.getCode() == KeyCode.BACK_SPACE) {
                    if (comboBox1.getEditor().getText() != null) {
                        moveCaretToPos = true;
                        caretPos = comboBox1.getEditor().getCaretPosition();
                    }
                } else if (event.getCode() == KeyCode.DELETE) {
                    if (comboBox1.getEditor().getText() != null) {
                        moveCaretToPos = true;
                        caretPos = comboBox1.getEditor().getCaretPosition();
                    }
                } else if (event.getCode() == KeyCode.ENTER) {
                    return;
                }

                if (event.getCode() == KeyCode.RIGHT
                        || event.getCode() == KeyCode.LEFT
                        || event.getCode().equals(KeyCode.SHIFT)
                        || event.getCode().equals(KeyCode.CONTROL)
                        || event.isControlDown()
                        || event.getCode() == KeyCode.HOME
                        || event.getCode() == KeyCode.END
                        || event.getCode() == KeyCode.TAB) {
                    return;
                }

                ObservableList<T> list = FXCollections.observableArrayList();
                for (T aData : data) {
                    if (aData != null && comboBox1.getEditor().getText() != null
                            && comparatorMethod.matches(comboBox1.getEditor().getText(), aData)) {
                        list.add(aData);
                    }
                }
                String t = "";
                if (comboBox1.getEditor().getText() != null) {
                    t = comboBox1.getEditor().getText();
                }

                comboBox1.setItems(list);
                comboBox1.getEditor().setText(t);
                if (!moveCaretToPos) {
                    caretPos = -1;
                }
                moveCaret(t.length());
                if (!list.isEmpty()) {
                    comboBox1.show();
                }
            }

            private void moveCaret(int textLength) {
                if (caretPos == -1) {
                    comboBox1.getEditor().positionCaret(textLength);
                } else {
                    comboBox1.getEditor().positionCaret(caretPos);
                }
                moveCaretToPos = false;
            }
        });
    }

    public static<T> T getComboBoxValue(ComboBox<T> comboBox){
        if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
            return null;
        } else {
            return comboBox.getItems().get(comboBox.getSelectionModel().getSelectedIndex());
        }
    }

}