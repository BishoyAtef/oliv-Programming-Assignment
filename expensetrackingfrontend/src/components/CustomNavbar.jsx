import { NavLink, Link } from 'react-router-dom'
import { useState } from "react";
import { Menu, X } from "lucide-react";
import logo from '../assets/imgs/logo.png'

const CustomNavbar = () => {
    const [isOpen, setIsOpen] = useState(false);

    const linkClass = ({ isActive }) =>
        isActive
            ? "bg-indigo-600 text-white rounded-md px-4 py-2 transition"
            : "text-white hover:bg-indigo-500 hover:text-white rounded-md px-4 py-2 transition";

    return (
        <nav className="bg-gray-800 border-b border-gray-700 shadow-md">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex h-16 items-center justify-between">
            <Link to="/" className="flex items-center">
                <img src={logo} alt="Oliv" className="h-10 w-auto" />
                <span className="text-white text-xl font-bold ml-2 hidden md:inline">
                Expense Tracking
                </span>
            </Link>

            {/* <div className="hidden md:flex space-x-4">
                <NavLink to="/" className={linkClass}>
                Back
                </NavLink>
            </div> */}

            <button
                onClick={() => setIsOpen(!isOpen)}
                className="text-white md:hidden"
            >
                {isOpen ? <X size={28} /> : <Menu size={28} />}
            </button>
            </div>
        </div>

        {isOpen && (
            <div className="md:hidden px-4 pb-4 pt-2 bg-gray-700 space-y-2 transition-all">
            <NavLink to="/" className={linkClass} onClick={() => setIsOpen(false)}>
                Back
            </NavLink>
            </div>
        )}
        </nav>
    );
}

export default CustomNavbar