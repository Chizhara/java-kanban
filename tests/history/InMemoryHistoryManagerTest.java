package history;

import manager.history.InMemoryHistoryManager;

public class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {
    @Override
    protected void init() {
        historyManager = new InMemoryHistoryManager();
    }
}
