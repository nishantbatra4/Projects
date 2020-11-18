public class ContainerVisitor implements Visitor {

    private double currPrice;
    private double marketValue;

    @Override
    public void visit(ItemContainer container) {
        currPrice += container.getPrice();
    }

    @Override
    public void visit(Item item) {
        currPrice += item.getPrice();
        marketValue += item.getMarketVal();
    }

    @Override
    public double getCurrPrice() {
        return currPrice;
    }

    @Override
    public double getMarketValue(){
        return marketValue;
    }
}
