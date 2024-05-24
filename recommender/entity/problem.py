from sqlalchemy import Column, Integer, String, Float, BigInteger, Table, ForeignKey
from sqlalchemy.orm import relationship
from sqlalchemy.ext.declarative import declarative_base

from entity import Base
from entity.problem_tag import ProblemTag


class Problem(Base):
   __tablename__ = 'problem'

   id = Column(BigInteger, primary_key=True)
   title = Column(String)
   tier = Column(Integer)
   accepted_user_count = Column(Integer)
   average_tries = Column(Float)
