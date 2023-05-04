package history;

import manager.InMemoryHistoryManager;

public class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {
    @Override
    protected void init() {
        historyManager = new InMemoryHistoryManager();
    }
}
