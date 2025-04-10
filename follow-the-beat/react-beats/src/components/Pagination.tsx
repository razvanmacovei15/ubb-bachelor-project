import React from "react";
import classnames from "classnames";
import { usePagination, DOTS } from "../hooks/usePagination";
import "./pagination.css";

interface PaginationProps {
  onPageChange: (page: number) => void;
  totalCount: number;
  siblingCount?: number;
  currentPage: number;
  pageSize: number;
  className?: string;
}

const Pagination: React.FC<PaginationProps> = ({
  onPageChange,
  totalCount,
  siblingCount = 1,
  currentPage,
  pageSize,
  className,
}) => {
  const paginationRange = usePagination({
    currentPage,
    totalCount,
    siblingCount,
    pageSize,
  });

  if (currentPage === 0 || !paginationRange || paginationRange.length < 2) {
    return null;
  }

  const onNext = () => {
    onPageChange(currentPage + 1);
  };

  const onPrevious = () => {
    onPageChange(currentPage - 1);
  };

  const lastPage = paginationRange[paginationRange.length - 1];

  return (
    <ul className={classnames("pagination-container", className)}>
      <li
        className={classnames("pagination-item", {
          disabled: currentPage === 1,
        })}
        onClick={currentPage === 1 ? undefined : onPrevious}
      >
        <div className="arrow left" />
      </li>

      {paginationRange.map((pageNumber, index) => {
        if (pageNumber === DOTS) {
          return (
            <li key={index} className="pagination-item dots">
              &#8230;
            </li>
          );
        }

        return (
          <li
            key={index}
            className={classnames("pagination-item", {
              selected: pageNumber === currentPage,
            })}
            onClick={() => onPageChange(Number(pageNumber))}
          >
            {pageNumber}
          </li>
        );
      })}

      <li
        className={classnames("pagination-item", {
          disabled: currentPage === lastPage,
        })}
        onClick={currentPage === lastPage ? undefined : onNext}
      >
        <div className="arrow right" />
      </li>
    </ul>
  );
};

export default Pagination;
