public interface Visitor {
    void visit(ItemContainer container);
    void visit(Item item);
    double getCurrPrice();
    double getMarketValue();
}
