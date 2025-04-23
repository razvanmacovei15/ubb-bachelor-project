import React from 'react';
import './FilterSidebar.css';

interface FilterSidebarProps {
    timeRange: string;
    onTimeRangeChange: (range: string) => void;
    onSortChange: (sortBy: string) => void;
}

const FilterSidebar: React.FC<FilterSidebarProps> = ({ 
    timeRange, 
    onTimeRangeChange,
    onSortChange 
}) => {
    return (
        <div className="filter-sidebar">
            <h3>Filters</h3>
            
            <div className="filter-section">
                <h4>Time Range</h4>
                <div className="filter-options">
                    <button 
                        className={timeRange === 'LAST_4_WEEKS' ? 'active' : ''}
                        onClick={() => onTimeRangeChange('LAST_4_WEEKS')}
                    >
                        Last 4 Weeks
                    </button>
                    <button 
                        className={timeRange === 'LAST_6_MONTHS' ? 'active' : ''}
                        onClick={() => onTimeRangeChange('LAST_6_MONTHS')}
                    >
                        Last 6 Months
                    </button>
                    <button 
                        className={timeRange === 'ALL_TIME' ? 'active' : ''}
                        onClick={() => onTimeRangeChange('ALL_TIME')}
                    >
                        All Time
                    </button>
                </div>
            </div>

            <div className="filter-section">
                <h4>Sort By</h4>
                <div className="filter-options">
                    <button onClick={() => onSortChange('popularity')}>
                        Popularity
                    </button>
                    <button onClick={() => onSortChange('name')}>
                        Name
                    </button>
                </div>
            </div>
        </div>
    );
};

export default FilterSidebar; 