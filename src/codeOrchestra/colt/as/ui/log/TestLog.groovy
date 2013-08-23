package codeOrchestra.colt.as.ui.log

import codeOrchestra.colt.core.logging.Level
import codeOrchestra.colt.core.ui.components.log.LogMessage
import javafx.collections.FXCollections

/**
 * @author Eugene Potapenko
 */
class TestLog {
    public static javafx.collections.ObservableList<LogMessage> createTestLogList() {
        javafx.collections.ObservableList<LogMessage> result = FXCollections.observableArrayList()
        (10).times {
            result.add(new LogMessage("com.codeOrchestra.*:8", Level.WARN, """ListView Selection / Focus APIs To track selection and focus, it is necessary to become familiar with the SelectionModel and FocusModel classes. A ListView has at most one instance of each of these classes, available from selectionModel and focusModel properties respectively. Whilst it is possible to use this API to set a new selection model, in most circumstances this is not necessary - the default selection and focus models should work in most circumstances. The default SelectionModel used when instantiating a ListView is an implementation of the MultipleSelectionModel abstract class. However, as noted in the API documentation for the selectionMode property, the default value is SelectionMode.SINGLE. To enable multiple selection in a default ListView instance, it is therefore necessary to do the following: listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);""", 10, ""))
            result.add(new LogMessage("com.codeOrchestra.*:8", Level.TRACE, """ A ListView displays a horizontal or vertical list of items from which the user may select, or with which the user may interact. A ListView is able to have its generic type set to represent the type of data in the backing model. Doing this has the benefit of making various methods in the ListView, as well as the supporting classes (mentioned below), type-safe. In addition, making use of the generic supports substantially simplifies development of applications making use of ListView, as all modern IDEs are able to auto-complete far more successfully with the additional type information. """, 10, ""))
            result.add(new LogMessage("com.codeOrchestra.*:18", Level.FATAL, """ A simple example of how to create and populate a ListView of names (Strings) is shown here: """, 10, ""))
            result.add(new LogMessage("com.codeOrchestra.*:8", Level.ERROR, """ The elements of the ListView are contained within the items ObservableList. This ObservableList is automatically observed by the ListView, such that any changes that occur inside the ObservableList will be automatically shown in the ListView itself. If passying the ObservableList in to the ListView constructor is not feasible, the recommended approach for setting the items is to simply call: """, 10, ""))
            result.add(new LogMessage("com.codeOrchestra.*:28", Level.INFO, """ The elements of the ListView are contained within the items ObservableList. This ObservableList is automatically observed by the ListView, such that any changes that occur inside the ObservableList will be automatically shown in the ListView itself. If passying the ObservableList in to the ListView constructor is not feasible, the recommended approach for setting the items is to simply call: """, 10, ""))
            result.add(new LogMessage("com.codeOrchestra.*:33", Level.TRACE, """The issue with the approach shown above is that the content list is being copied into the items list - meaning that subsequent changes to the content list are not observed, and will not be reflected visually within the ListView. """, 10, ""))
            result.add(new LogMessage("com.codeOrchestra.*:33", Level.TRACE, (0..10000).join(""), 10, ""))
            result.add(new LogMessage("", Level.WARN, """ The issue with the approach shown above is that the content list is being copied into the items list - meaning that subsequent changes to the content list are not observed, and will not be reflected visually within the ListView. """, 10, ""))
        }
        return result;
    }
}
