from sqlalchemy import Column, BigInteger, DateTime, String, Enum
from sqlalchemy.orm import relationship
from sqlalchemy.ext.declarative import declarative_base
from entity import Base



class Member(Base):
    __tablename__ = 'member'

    id = Column(BigInteger, primary_key=True, autoincrement=True)
    created_at = Column(DateTime(6))
    modified_at = Column(DateTime(6))
    email = Column(String(255), unique=True, nullable=False)
    nickname = Column(String(255), unique=True, nullable=False)
    password = Column(String(255), nullable=False)
    profile_img = Column(String(255), nullable=False)
    role = Column(Enum('USER', 'ADMIN'), nullable=False)

