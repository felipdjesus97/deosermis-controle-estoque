-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 27/01/2026 às 17:33
-- Versão do servidor: 10.4.32-MariaDB
-- Versão do PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `loja`
--
CREATE DATABASE IF NOT EXISTS `loja` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `loja`;

-- --------------------------------------------------------

--
-- Estrutura para tabela `entradadeproduto`
--

CREATE TABLE `entradadeproduto` (
  `Codigo` bigint(20) UNSIGNED NOT NULL,
  `ProdutoID` bigint(20) UNSIGNED NOT NULL,
  `Quantidade` int(9) NOT NULL,
  `ValorDaCompra` decimal(15,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `formadepagamento`
--

CREATE TABLE `formadepagamento` (
  `Codigo` int(1) NOT NULL,
  `Descricao` varchar(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `fornecedor`
--

CREATE TABLE `fornecedor` (
  `Codigo` bigint(20) UNSIGNED NOT NULL,
  `Nome` varchar(50) NOT NULL,
  `CPFCNPJ` varchar(14) NOT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `Instagram` varchar(50) DEFAULT NULL,
  `WhatsApp` char(11) DEFAULT NULL,
  `Site` varchar(150) DEFAULT NULL,
  `UF` char(2) DEFAULT NULL,
  `Cidade` varchar(50) DEFAULT NULL,
  `Bairro` varchar(50) DEFAULT NULL,
  `CEP` char(8) DEFAULT NULL,
  `Logradouro` varchar(100) DEFAULT NULL,
  `Numero` varchar(15) DEFAULT NULL,
  `Complemento` varchar(30) DEFAULT NULL,
  `Observacao` text DEFAULT NULL,
  `Inativo` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `fotodoproduto`
--

CREATE TABLE `fotodoproduto` (
  `CodigoDaFoto` bigint(20) UNSIGNED NOT NULL,
  `ProdutoID` bigint(20) UNSIGNED NOT NULL,
  `Imagem` longblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `funcionalidades`
--

CREATE TABLE `funcionalidades` (
  `Codigo` int(2) NOT NULL,
  `Nome` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `pedidoproduto`
--

CREATE TABLE `pedidoproduto` (
  `Codigo` bigint(20) UNSIGNED NOT NULL,
  `Data` date NOT NULL,
  `Desconto` decimal(5,2) NOT NULL,
  `ValorSubTotal` decimal(20,2) NOT NULL,
  `ValorTotal` decimal(20,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `pedido_formadepagamento`
--

CREATE TABLE `pedido_formadepagamento` (
  `PedidoID` bigint(20) UNSIGNED NOT NULL,
  `FormaDePagamentoID` int(1) NOT NULL,
  `ValorPago` decimal(15,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `pedido_saida`
--

CREATE TABLE `pedido_saida` (
  `PedidoID` bigint(20) UNSIGNED NOT NULL,
  `SaidaID` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `permissao`
--

CREATE TABLE `permissao` (
  `UsuarioID` bigint(20) UNSIGNED NOT NULL,
  `FuncionalidadeID` int(1) NOT NULL,
  `Permitir` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `produto`
--

CREATE TABLE `produto` (
  `Codigo` bigint(20) UNSIGNED NOT NULL,
  `Nome` varchar(50) NOT NULL,
  `Quantidade` int(9) NOT NULL DEFAULT 0,
  `QuantidadeMinima` int(9) NOT NULL DEFAULT 0,
  `ValorUnitario` decimal(15,2) NOT NULL DEFAULT 0.00,
  `Inativo` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `produto_fornecedor`
--

CREATE TABLE `produto_fornecedor` (
  `ProdutoID` bigint(20) UNSIGNED NOT NULL,
  `FornecedorID` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `reposicaoproduto`
--

CREATE TABLE `reposicaoproduto` (
  `Codigo` bigint(20) UNSIGNED NOT NULL,
  `Data` date NOT NULL,
  `FornecedorID` bigint(20) UNSIGNED NOT NULL,
  `ValorDaCompra` decimal(20,2) NOT NULL,
  `ChaveAcessoNFe` char(44) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `reposicao_entrada`
--

CREATE TABLE `reposicao_entrada` (
  `ReposicaoID` bigint(20) UNSIGNED NOT NULL,
  `EntradaID` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `saidadeproduto`
--

CREATE TABLE `saidadeproduto` (
  `Codigo` bigint(20) UNSIGNED NOT NULL,
  `ProdutoID` bigint(20) UNSIGNED NOT NULL,
  `FornecedorID` bigint(20) UNSIGNED DEFAULT NULL,
  `Quantidade` int(9) NOT NULL,
  `ValorDaVenda` decimal(15,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `usuario`
--

CREATE TABLE `usuario` (
  `Codigo` bigint(20) UNSIGNED NOT NULL,
  `Nome` varchar(50) NOT NULL,
  `Senha` varchar(255) NOT NULL,
  `Salt` varchar(255) NOT NULL,
  `Tipo` tinyint(1) NOT NULL DEFAULT 0,
  `Email` varchar(100) NOT NULL,
  `SenhaTemporaria` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `entradadeproduto`
--
ALTER TABLE `entradadeproduto`
  ADD PRIMARY KEY (`Codigo`),
  ADD KEY `ProdutoID` (`ProdutoID`);

--
-- Índices de tabela `formadepagamento`
--
ALTER TABLE `formadepagamento`
  ADD PRIMARY KEY (`Codigo`),
  ADD UNIQUE KEY `Descricao` (`Descricao`);

--
-- Índices de tabela `fornecedor`
--
ALTER TABLE `fornecedor`
  ADD PRIMARY KEY (`Codigo`);

--
-- Índices de tabela `fotodoproduto`
--
ALTER TABLE `fotodoproduto`
  ADD PRIMARY KEY (`CodigoDaFoto`),
  ADD KEY `ProdutoID` (`ProdutoID`);

--
-- Índices de tabela `funcionalidades`
--
ALTER TABLE `funcionalidades`
  ADD PRIMARY KEY (`Codigo`),
  ADD UNIQUE KEY `Nome` (`Nome`);

--
-- Índices de tabela `pedidoproduto`
--
ALTER TABLE `pedidoproduto`
  ADD PRIMARY KEY (`Codigo`);

--
-- Índices de tabela `pedido_formadepagamento`
--
ALTER TABLE `pedido_formadepagamento`
  ADD PRIMARY KEY (`PedidoID`,`FormaDePagamentoID`),
  ADD KEY `fk_ped_forma_forma` (`FormaDePagamentoID`);

--
-- Índices de tabela `pedido_saida`
--
ALTER TABLE `pedido_saida`
  ADD PRIMARY KEY (`PedidoID`,`SaidaID`),
  ADD KEY `SaidaID` (`SaidaID`);

--
-- Índices de tabela `permissao`
--
ALTER TABLE `permissao`
  ADD UNIQUE KEY `uq_perm_user_func` (`UsuarioID`,`FuncionalidadeID`),
  ADD KEY `fk_perm_func` (`FuncionalidadeID`);

--
-- Índices de tabela `produto`
--
ALTER TABLE `produto`
  ADD PRIMARY KEY (`Codigo`);

--
-- Índices de tabela `produto_fornecedor`
--
ALTER TABLE `produto_fornecedor`
  ADD PRIMARY KEY (`ProdutoID`,`FornecedorID`),
  ADD KEY `FornecedorID` (`FornecedorID`);

--
-- Índices de tabela `reposicaoproduto`
--
ALTER TABLE `reposicaoproduto`
  ADD PRIMARY KEY (`Codigo`),
  ADD KEY `FornecedorID` (`FornecedorID`);

--
-- Índices de tabela `reposicao_entrada`
--
ALTER TABLE `reposicao_entrada`
  ADD PRIMARY KEY (`ReposicaoID`,`EntradaID`),
  ADD KEY `EntradaID` (`EntradaID`);

--
-- Índices de tabela `saidadeproduto`
--
ALTER TABLE `saidadeproduto`
  ADD PRIMARY KEY (`Codigo`),
  ADD KEY `ProdutoID` (`ProdutoID`),
  ADD KEY `FornecedorID` (`FornecedorID`);

--
-- Índices de tabela `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`Codigo`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `entradadeproduto`
--
ALTER TABLE `entradadeproduto`
  MODIFY `Codigo` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `formadepagamento`
--
ALTER TABLE `formadepagamento`
  MODIFY `Codigo` int(1) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `fornecedor`
--
ALTER TABLE `fornecedor`
  MODIFY `Codigo` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `fotodoproduto`
--
ALTER TABLE `fotodoproduto`
  MODIFY `CodigoDaFoto` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `funcionalidades`
--
ALTER TABLE `funcionalidades`
  MODIFY `Codigo` int(2) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `pedidoproduto`
--
ALTER TABLE `pedidoproduto`
  MODIFY `Codigo` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `produto`
--
ALTER TABLE `produto`
  MODIFY `Codigo` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `reposicaoproduto`
--
ALTER TABLE `reposicaoproduto`
  MODIFY `Codigo` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `saidadeproduto`
--
ALTER TABLE `saidadeproduto`
  MODIFY `Codigo` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `usuario`
--
ALTER TABLE `usuario`
  MODIFY `Codigo` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `entradadeproduto`
--
ALTER TABLE `entradadeproduto`
  ADD CONSTRAINT `entradadeproduto_ibfk_1` FOREIGN KEY (`ProdutoID`) REFERENCES `produto` (`Codigo`);

--
-- Restrições para tabelas `fotodoproduto`
--
ALTER TABLE `fotodoproduto`
  ADD CONSTRAINT `fotodoproduto_ibfk_1` FOREIGN KEY (`ProdutoID`) REFERENCES `produto` (`Codigo`) ON DELETE CASCADE;

--
-- Restrições para tabelas `pedido_formadepagamento`
--
ALTER TABLE `pedido_formadepagamento`
  ADD CONSTRAINT `fk_ped_forma_forma` FOREIGN KEY (`FormaDePagamentoID`) REFERENCES `formadepagamento` (`Codigo`),
  ADD CONSTRAINT `fk_ped_forma_pedido` FOREIGN KEY (`PedidoID`) REFERENCES `pedidoproduto` (`Codigo`) ON DELETE CASCADE;

--
-- Restrições para tabelas `pedido_saida`
--
ALTER TABLE `pedido_saida`
  ADD CONSTRAINT `pedido_saida_ibfk_1` FOREIGN KEY (`PedidoID`) REFERENCES `pedidoproduto` (`Codigo`) ON DELETE CASCADE,
  ADD CONSTRAINT `pedido_saida_ibfk_2` FOREIGN KEY (`SaidaID`) REFERENCES `saidadeproduto` (`Codigo`) ON DELETE CASCADE;

--
-- Restrições para tabelas `permissao`
--
ALTER TABLE `permissao`
  ADD CONSTRAINT `fk_perm_func` FOREIGN KEY (`FuncionalidadeID`) REFERENCES `funcionalidades` (`Codigo`),
  ADD CONSTRAINT `fk_perm_usuario` FOREIGN KEY (`UsuarioID`) REFERENCES `usuario` (`Codigo`);

--
-- Restrições para tabelas `produto_fornecedor`
--
ALTER TABLE `produto_fornecedor`
  ADD CONSTRAINT `produto_fornecedor_ibfk_1` FOREIGN KEY (`ProdutoID`) REFERENCES `produto` (`Codigo`) ON DELETE CASCADE,
  ADD CONSTRAINT `produto_fornecedor_ibfk_2` FOREIGN KEY (`FornecedorID`) REFERENCES `fornecedor` (`Codigo`);

--
-- Restrições para tabelas `reposicaoproduto`
--
ALTER TABLE `reposicaoproduto`
  ADD CONSTRAINT `reposicaoproduto_ibfk_1` FOREIGN KEY (`FornecedorID`) REFERENCES `fornecedor` (`Codigo`);

--
-- Restrições para tabelas `reposicao_entrada`
--
ALTER TABLE `reposicao_entrada`
  ADD CONSTRAINT `reposicao_entrada_ibfk_1` FOREIGN KEY (`ReposicaoID`) REFERENCES `reposicaoproduto` (`Codigo`) ON DELETE CASCADE,
  ADD CONSTRAINT `reposicao_entrada_ibfk_2` FOREIGN KEY (`EntradaID`) REFERENCES `entradadeproduto` (`Codigo`) ON DELETE CASCADE;

--
-- Restrições para tabelas `saidadeproduto`
--
ALTER TABLE `saidadeproduto`
  ADD CONSTRAINT `saideproduto_ibfk_1` FOREIGN KEY (`ProdutoID`) REFERENCES `produto` (`Codigo`),
  ADD CONSTRAINT `saideproduto_ibfk_2` FOREIGN KEY (`FornecedorID`) REFERENCES `fornecedor` (`Codigo`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
