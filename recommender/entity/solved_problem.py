from sqlalchemy import Column, BigInteger, DateTime, Enum, ForeignKey
from sqlalchemy.orm import relationship, Mapped, mapped_column
from sqlalchemy.ext.declarative import declarative_base
from entity import Base


class SolvedProblem(Base):
    __tablename__ = 'solved_problem'

    id = Column(BigInteger, primary_key=True, autoincrement=True)
    created_at = Column(DateTime(6))
    modified_at = Column(DateTime(6))
    complete = Column(Enum('NOT_YET', 'DONE'))
    uploaded_at = Column(DateTime(6))
    

    member_id : Mapped[int] = mapped_column(ForeignKey('member.id'))
    member : Mapped["Member"] = relationship()

    problem_id : Mapped[int] = mapped_column(ForeignKey('problem.id'))
    problem : Mapped["Problem"] = relationship()