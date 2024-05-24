from sqlalchemy import Column, Integer, String, ForeignKey
from entity import Base

class ProblemTag(Base):
    __tablename__ = 'problem_tag'

    problem_id = Column(Integer, ForeignKey('problem.id'), primary_key=True)
    tag_id = Column(Integer, ForeignKey('tag.id'), primary_key=True)