"""
Credit Card Core System - Python Implementation for Testing
This provides a Python version of the core logic for testing purposes.
"""

import unittest
from dataclasses import dataclass
from typing import Optional
from enum import Enum
import math


class MemberStatus(Enum):
    PENDING = "PENDING"
    ACTIVE = "ACTIVE"
    SUSPENDED = "SUSPENDED"
    CLOSED = "CLOSED"


class TransactionType(Enum):
    AUTH = "AUTH"
    CAPTURE = "CAPTURE"
    REFUND = "REFUND"
    PAYMENT = "PAYMENT"
    CHARGEBACK = "CHARGEBACK"
    INSTALLMENT = "INSTALLMENT"


class TransactionStatus(Enum):
    PENDING = "PENDING"
    APPROVED = "APPROVED"
    DECLINED = "DECLINED"
    SETTLED = "SETTLED"
    CANCELLED = "CANCELLED"
    DISPUTED = "DISPUTED"


class RiskLevel(Enum):
    LOW = "LOW"
    MEDIUM = "MEDIUM"
    HIGH = "HIGH"
    CRITICAL = "CRITICAL"


@dataclass
class Member:
    member_number: str
    name_kanji: str
    email: str
    status: MemberStatus
    credit_limit: float
    current_balance: float


@dataclass
class Transaction:
    transaction_id: str
    member_number: str
    type: TransactionType
    amount: float
    status: TransactionStatus
    authorization_code: Optional[str] = None


class CreditLimitExceededException(Exception):
    pass


class FraudDetectedException(Exception):
    pass


class CreditManagementService:
    """Credit Management Service - 与信管理サービス"""
    
    def __init__(self):
        self.members = {}
    
    def authorize(self, member_number: str, amount: float, 
                  merchant_name: str, merchant_category: str) -> Transaction:
        """
        オーソリゼーション（与信確保）処理
        Authorization - Reserve credit for a transaction
        """
        # 1. 会員取得
        member = self._get_member(member_number)
        
        # 2. 与信限度額チェック
        available_credit = member.credit_limit - member.current_balance
        if available_credit < amount:
            raise CreditLimitExceededException("与信限度額を超過しました")
        
        # 3. 不正検知チェック
        fraud_alerts = self._check_fraud_risk(member_number, amount, merchant_category)
        if fraud_alerts:
            raise FraudDetectedException(f"不正検知されました: {', '.join(fraud_alerts)}")
        
        # 4. オーソリゼーション生成
        tx = Transaction(
            transaction_id=self._generate_transaction_id(),
            member_number=member_number,
            type=TransactionType.AUTH,
            amount=amount,
            status=TransactionStatus.APPROVED,
            authorization_code=self._generate_auth_code()
        )
        
        # 5. 与信確保（利用可能枠減少）
        member.current_balance += amount
        
        return tx
    
    def void_transaction(self, transaction_id: str) -> Transaction:
        """取消処理"""
        # 簡略実装
        return Transaction(
            transaction_id=transaction_id,
            member_number="M000000000",
            type=TransactionType.AUTH,
            amount=0,
            status=TransactionStatus.CANCELLED
        )
    
    def _get_member(self, member_number: str) -> Member:
        """会員取得（モック）"""
        return Member(
            member_number=member_number,
            name_kanji="テスト太郎",
            email="test@example.com",
            status=MemberStatus.ACTIVE,
            credit_limit=500000,
            current_balance=100000
        )
    
    def _check_fraud_risk(self, member_number: str, amount: float, 
                         merchant_category: str) -> list:
        """不正リスクチェック"""
        alerts = []
        
        # 高額チェック
        if amount > 100000:
            alerts.append("高額の取引です")
        
        # リスク業種チェック
        risky_categories = ["gambling", "casino", "adult"]
        if merchant_category.lower() in risky_categories:
            alerts.append("リスクの高い業種です")
        
        return alerts
    
    def _generate_transaction_id(self) -> str:
        """取引ID生成"""
        import random
        import string
        return "TX2026" + ''.join(random.choices(string.digits, k=9))
    
    def _generate_auth_code(self) -> str:
        """承認番号生成"""
        import random
        return str(random.randint(100000, 999999))


# ============== Unit Tests ==============

class TestCreditManagementService(unittest.TestCase):
    """Test cases for Credit Management Service"""
    
    def setUp(self):
        self.service = CreditManagementService()
    
    def test_authorize_success(self):
        """正常系：オーソリゼーション成功"""
        result = self.service.authorize(
            member_number="M123456789",
            amount=10000,
            merchant_name="Amazon Japan",
            merchant_category="retail"
        )
        
        self.assertIsNotNone(result)
        self.assertIsNotNone(result.transaction_id)
        self.assertIsNotNone(result.authorization_code)
        self.assertEqual(result.status, TransactionStatus.APPROVED)
        self.assertEqual(result.type, TransactionType.AUTH)
    
    def test_authorize_credit_limit_exceeded(self):
        """異常系：与信限度額超過"""
        with self.assertRaises(CreditLimitExceededException):
            self.service.authorize(
                member_number="M123456789",
                amount=1000000,  # 限度額超
                merchant_name="Amazon Japan",
                merchant_category="retail"
            )
    
    def test_authorize_fraud_high_amount(self):
        """異常系：不正検知（高額）」"""
        with self.assertRaises(FraudDetectedException):
            self.service.authorize(
                member_number="M123456789",
                amount=200000,  # 高額
                merchant_name="Luxury Store",
                merchant_category="retail"
            )
    
    def test_authorize_fraud_risky_category(self):
        """異常系：不正検知（リスク業種）」"""
        with self.assertRaises(FraudDetectedException):
            self.service.authorize(
                member_number="M123456789",
                amount=50000,
                merchant_name="Casino",
                merchant_category="gambling"
            )
    
    def test_void_transaction(self):
        """正常系：取消処理成功"""
        result = self.service.void_transaction("TX2026123456789")
        
        self.assertIsNotNone(result)
        self.assertEqual(result.status, TransactionStatus.CANCELLED)


# ============== Run Tests ==============

if __name__ == "__main__":
    print("="*60)
    print("🧪 Running Credit Card Core System Tests")
    print("="*60)
    
    # Run tests with verbosity
    unittest.main(verbosity=2)
