package BaiTap;

public class OrderItem {
    private Product product;
    private int soLuong;

    public OrderItem(Product product, int soLuong) {
        this.product = product;
        this.soLuong = soLuong;
    }

    public double tinhThanhTien() {
        return product.getGia() * soLuong;
    }

    public Product getProduct() {
        return product;
    }

    public int getSoLuong() {
        return soLuong;
    }

    @Override
    public String toString() {
        return product.getTen() + 
               " | SL: " + soLuong + 
               " | Thành tiền: " + tinhThanhTien();
    }
}
