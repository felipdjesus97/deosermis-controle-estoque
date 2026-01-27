
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class CalculoPedidoTest {

    @Test
    public void deveCalcularTotalPedidoComQuantidadeEComDesconto() {
        int quantidade = 3;
        BigDecimal valorUnitario = new BigDecimal("49.90");
        BigDecimal desconto = new BigDecimal("20.00");

        BigDecimal subtotal = valorUnitario.multiply(BigDecimal.valueOf(quantidade));
        BigDecimal totalFinal = subtotal.subtract(desconto);

        assertEquals(new BigDecimal("129.70"), totalFinal);
    }

    @Test
    public void deveCalcularTotalPedidoSemDesconto() {
        int quantidade = 2;
        BigDecimal valorUnitario = new BigDecimal("35.50");
        BigDecimal desconto = BigDecimal.ZERO;

        BigDecimal subtotal = valorUnitario.multiply(BigDecimal.valueOf(quantidade));
        BigDecimal totalFinal = subtotal.subtract(desconto);

        assertEquals(new BigDecimal("71.00"), totalFinal);
    }
}
