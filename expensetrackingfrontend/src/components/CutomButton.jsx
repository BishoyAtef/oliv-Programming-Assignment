import { useState } from "react";

const CustomButton = ({onSuccess, label="Fetch", disabled=false, className=""}) => {
    const [error, setError] = useState(null);
    const handleClick = async () => {
        try {
            setError(null);
            onSuccess();
        } catch (err) {
            setError("Failed to fetch data.");
            console.error(err);
        } 
    };
    return (
        <div className="text-center">
            <button
            onClick={handleClick}
            className={`${className}`}
            disabled={disabled}
            >
            {label}
            </button>
            {error && <div className="text-red-500 text-sm mt-2">{error}</div>}
        </div>
    );
};

export default CustomButton;